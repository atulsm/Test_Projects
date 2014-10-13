function Connector(uuid) {
    if (typeof uuid != "undefined" && typeof ESM != "undefined") {
        this.UUID = uuid;
        this.connectorObject = ESM.groupForId(this.UUID);
    }
    this.start = function () {
        if (typeof this.connectorObject != "undefined") {
            this.connectorObject.start();
        }
    };
    this.stop = function () {
        if (typeof this.connectorObject != "undefined") {
            this.connectorObject.stop();
        }
    };
    if (typeof scriptEnv != "undefined") {
        return true;
    }
    if (instance.CONFIG.params.Conn_Retry != "none") {
        var queue = new java.util.concurrent.SynchronousQueue();
        this.readThread = java.lang.Thread(function () {
            var ctx = instance.CONFIG.scriptContext;
            while (ctx.getKeepRunning()) {
                queue.put(ctx.readData());
            }
        });
        this.readThread.setName("collector-read-thread-" + this.UUID);
        this.readThread.setDaemon(true);
        this.readThread.start();
        this.retries = 1;

        function getData(secs) {
            if (typeof secs == "undefined") {
                return queue.poll(0, java.util.concurrent.TimeUnit.SECONDS);
            }
            if (secs > 0) {
                return queue.poll(secs, java.util.concurrent.TimeUnit.SECONDS);
            } else if (secs == 0) {
                return queue.take();
            }
        }
    }
    this.read = function () {
        if (instance.CONFIG.rewind && typeof rec != "undefined") {
            instance.CONFIG.rewind = false;
            return rec;
        }
        var record = new Record();
        var needQuery = false;
        try {
            if (instance.CONFIG.params.Conn_Retry != "none") {
                if (instance.CONFIG.params.Conn_Retry == "fixed") {
                    record.connectorData = getData(instance.CONFIG.params.RX_Timeout);
                } else {
                    for (var i = 0; i < this.retries; i++) {
                        record.connectorData = getData(5);
                        if (record.connectorData != null || !ScriptEngineUtil.keepRunning()) {
                            if (String(record.connectorData.unmodifiableData).search(/Row_Left=-1/) != -1) {
                                needQuery = true;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            if (needQuery) {
                record.CONNECTION_STATUS = "NEED-QUERY";
            }
            if (record.connectorData === null || typeof record.CONNECTION_ERROR != "undefined" || String(record.connectorData.unmodifiableData).search(/Row_Left=-1/) != -1) {
                record.CONNECTION_ERROR = record.CONNECTION_ERROR ? record.CONNECTION_ERROR : "NODATA";
                if (this.retries < 24) {
                    this.retries = this.retries * 2;
                }
                return record;
            }
            this.retries = 1;
            var connectorUnmodData = record.connectorData.getUnmodifiableData();
            var entries = connectorUnmodData.entrySet().toArray();
            record.RXMap = {};
            for (var j = 0; j < entries.length; ++j) {
                var key = String(entries[j].key);
                var value = String(entries[j].value);
                if (key.length >= 2 && key.charAt(1) == '_' && (key.charAt(0) == 's' || key.charAt(0) == 'i')) {
                    record[key] = value;
                } else {
                    switch (key) {
                    case "CONNECTION_METHOD":
                    case "CONNECTION_MODE":
                    case "o":
                    case "Row_Left":
                    case "Application":
                    case "CurrentFile":
                    case "SourceIP":
                    case "QueryID":
                    case "EventSourceId":
                        record[key] = value;
                        break;
                    default:
                        record.RXMap[key] = value;
                        break;
                    }
                }
            }
        } catch (err) {
            record.CONNECTION_ERROR = err;
            log("Error reading next connector data object: " + err);
            return record;
        }
        if (typeof record.CONNECTION_METHOD != 'undefined' && record.CONNECTION_MODE == "Replay") {
            switch (record.s_RXBufferString[0]) {
            case "{":
                var tmpObj = JSON.parse(record.s_RXBufferString);
                break;
            default:
                var tmpString = record.s_RXBufferString.replace(/\"\"/g, "@QUOTE@");
                tmpString = tmpString.replace(/\"/g, "@EMBEDQUOTE@");
                tmpString = tmpString.replace(/@QUOTE@/g, "\"");
                tmpString = tmpString.trim();
                tmpString = tmpString.replace(/\\n/g, "\n");
                tmpString = tmpString.replace(/\/x0001/g, "\u0001");
                tmpString = tmpString.replace(/\/x0002/g, "\u0002");
                tmpString = tmpString.replace(/\/x0004/g, "\u0004");
                var tmpObj = tmpString.parseNVP(" ", "=", "\"");
                if (typeof tmpObj.s_RXBufferString != "undefined") {
                    tmpObj.s_RXBufferString = tmpObj.s_RXBufferString.replace(/@EMBEDQUOTE@/g, "\"");
                }
                if (typeof tmpObj.s_Body != "undefined") {
                    tmpObj.s_Body = tmpObj.s_Body.replace(/@EMBEDQUOTE@/g, "\"");
                }
                break;
            }
            for (var key in tmpObj) {
                if (tmpObj.hasOwnProperty(key)) {
                    var value = String(tmpObj[key]);
                    if (key.length >= 2 && key.charAt(1) == '_' && (key.charAt(0) == 's' || key.charAt(0) == 'i')) {
                        record[key] = value;
                    } else {
                        switch (key) {
                        case "CONNECTION_METHOD":
                        case "CONNECTION_MODE":
                        case "o":
                        case "Row_Left":
                        case "Application":
                        case "CurrentFile":
                        case "SourceIP":
                        case "QueryID":
                            record[key] = value;
                            break;
                        default:
                            record.RXMap[key] = value;
                            break;
                        }
                    }
                }
            }
        }
        if (record.Row_Left == -2) {
            record.CONNECTION_ERROR = "OFFSET_NOT_EVENT";
        }
        if (record.Row_Left == 0 || record.Row_Left == -1) {
            record.CONNECTION_STATUS = "NEED-QUERY";
        }
        return record;
    };
    this.send = function (msg) {
        var data = msg;
        if (msg instanceof SQLQuery) {
            data = msg.buildQuery();
        }
        return instance.CONFIG.scriptContext.sendTXData(data);
    };
    this.cleanup = function () {
        if (this.readThread != null) {
            this.readThread.interrupt();
            this.readThread = null;
        }
    }
    this.DBQuery = null;
}