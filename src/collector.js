function Collector(uuid) {
    if (typeof uuid != "undefined" && typeof ESM != "undefined") {
        this.UUID = uuid;
        this.collectorObject = ESM.collectorForId(this.UUID);
    }
    if (typeof scriptEnv == "undefined") {
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.AgentInfo);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.BaseEngine);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.BSFEngine);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.CollectorConfiguration);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.Constants);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.ContextInfoSender);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.DBCollectorHelper);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.EventData);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.DebugInfoSender);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.EventDataDebugger);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.FormatDate);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.InfoBlockSession);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.InfoSender);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.MatchResult);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.OSEnv);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.ScriptEngineContext);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.ScriptEngineUtil);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.TagValue);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.collector.util.FileUtil);
        importClass(Packages.esecurity.ccs.comp.evtsrcmgt.connectorapi.ConnectorData);
        importClass(java.util.logging.Level);
        importClass(java.util.Map);
        importClass(java.util.HashMap);
        this.CONFIG = {};
        this.CONFIG.params = {};
        this.CONFIG.scriptContext = ScriptEngineUtil.getContext();
        ScriptEngineUtil.log(Level.FINEST, "Initializing collector");
        this.UUID = String(this.CONFIG.scriptContext.getCollectorID());
        this.LOG = 0x1;
        this.EVENT = 0x2;
        this.CONFIG.collDir = String(this.CONFIG.scriptContext.getLocalDir()) + "/";
        this.CONFIG.esecDir = this.CONFIG.collDir.slice(0, this.CONFIG.collDir.indexOf("data"));
        this.CONFIG.commonDir = this.CONFIG.esecDir + "data/collector_common/";
        this.CONFIG.logDir = this.CONFIG.esecDir + "log/";
    }
    this.start = function () {
        if (typeof this.collectorObject != "undefined") {
            this.collectorObject.start();
        }
    };
    this.stop = function () {
        if (typeof this.collectorObject != "undefined") {
            this.collectorObject.stop();
        }
    };
}
Collector.prototype.scriptInit = function () {
    this.protoEvt = new Event();
    this.SEND_EVENT = false;
    this.STORE = {};
    this.PARSER = {};
    var packageFile = new File(this.CONFIG.collDir + "/package.xml");
    var packageText = packageFile.readFile();
    var packageXML = new XML(packageText.substr(packageText.indexOf("<", 1)));
    this.CONFIG.collectorScript = String(packageXML.Name);
    this.protoEvt.CollectorScript = this.CONFIG.collectorScript;
    var protoMap = new KeyMap(this.CONFIG.collDir + "/protoEvt.map");
    for (var field in protoMap) {
        if (field.hasOwnProperty && typeof protoMap[field] != "function" && field != "SourceFiles") {
            this.protoEvt[field] = protoMap.lookup(field)[0];
        }
    }
    var paramFile = new File(this.CONFIG.collDir + "/parameters.csv");
    inputLine = "null";
    linePair = [];
    for (inputLine = paramFile.readLine(); typeof inputLine != 'undefined'; inputLine = paramFile.readLine()) {
        linePair = inputLine.safesplit(",", "\"");
        this.CONFIG.params[linePair[0]] = linePair[1];
    }
    paramFile.close();
    if (typeof this.CONFIG.params.Conn_Retry == "undefined") {
        this.CONFIG.params.Conn_Retry = "falloff";
    }
    this.protoEvt.MSSPCustomerName = this.CONFIG.params.MSSP_Customer;
    this.protoEvt.Severity = this.CONFIG.params.Default_Severity;
    this.MAPS = {};
    this.MAPS.Rec2Evt = new DataMap(this.CONFIG.collDir + "/Rec2Evt.map");
    this.MAPS.Rec2Evt.makesafe();
    this.MAPS.Rec2Evt.compile();
    this.MAPS.UnsupEvt = new DataMap();
    this.MAPS.UnsupEvt.compile();
    this.MAPS.Evt2EvtData = new DataMap(this.CONFIG.collDir + "/Evt2EvtData.map");
    this.MAPS.tax = new KeyMap(this.CONFIG.collDir + "/taxonomy.map");
    this.MAPS.taxcode = new KeyMap(this.CONFIG.collDir + "/xdas_tax.map");
    this.MAPS.taxout = new KeyMap(this.CONFIG.collDir + "/xdas_out.map");
    if (this.CONFIG.params.Resolve_IP_and_Hostname == "yes") {
        this.MAPS.ip2n = new KeyMap(this.CONFIG.commonDir + "/ip2n.map");
        this.MAPS.n2ip = new KeyMap(this.CONFIG.commonDir + "/n2ip.map");
    }
    if (this.CONFIG.params.Resolve_IP_To_User == "yes") {
        this.MAPS.ip2u = new KeyMap(this.CONFIG.commonDir + "/ip2u.map");
    }
    this.MAPS.TZMap = [];
    if (this.CONFIG.params.ExecutionMode == "custom") {
        var customFile = new File(this.CONFIG.collDir + "custom.js");
        if (customFile.isOpen()) {
            try {
                eval(customFile.readFile());
                customFile.close();
            } catch (err) {
                log("Could not read from custom parsing file:" + err, 4, this.EVT);
            }
        }
        if (typeof this.customInit == "undefined" || typeof Record.prototype.customPreparse == "undefined" || typeof Record.prototype.customParse == "undefined") {
            this.CONFIG.params.ExecutionMode = "release";
            log("Custom parsing file not found or not complete; reverting to release mode", 4, this.EVENT & this.LOG);
        }
    }
    if (this.CONFIG.params.ExecutionMode != "custom") {
        Collector.prototype.customInit = function () {
            return true;
        }
        Record.prototype.customPreparse = function (e) {
            return true;
        }
        Record.prototype.customParse = function (e) {
            return true;
        }
    }
    if (this.CONFIG.params.ExecutionMode == "debug") {
        this.CONFIG.debugFile = new File(this.CONFIG.logDir + this.CONFIG.UUID + "-" + new Date().getTime() + ".json", "w");
    }
    if (this.CONFIG.params.Conn_Retry != "none") {
        do {
            rec = conn.read();
        } while (rec.CONNECTION_ERROR == "NODATA" && ScriptEngineUtil.keepRunning());
        if (rec.CONNECTION_METHOD == "DATABASE") {
            if (typeof this.CONFIG.params.Query_Variant != "undefined") {
                this.CONFIG.DBQuery = new SQLQuery("sqlquery" + this.CONFIG.params.Query_Variant + ".base");
            } else {
                this.CONFIG.DBQuery = new SQLQuery();
            }
            if (typeof this.CONFIG.params.Test_Query != "undefined" && this.CONFIG.params.Test_Query != "") {
                this.CONFIG.DBQuery.baseQuery = this.CONFIG.params.Test_Query;
            }
            if (rec.Row_Left == -2) {
                this.CONFIG.DBQuery.setOffset(rec.connectorData.offset);
                conn.send(this.CONFIG.DBQuery);
            }
        } else {
            this.CONFIG.rewind = true;
        }
    }
    return true;
};
Collector.prototype.sleep = function (sec) {
    var msec = sec * 1000;
    try {
        java.lang.Thread.sleep(msec);
    } catch (e) {
        log(e);
    }
    return true;
};
Collector.prototype.reset = function () {
    this.SEND_EVENT = false;
    if (this.CONFIG.params.Resolve_IP_and_Hostname == "yes") {
        this.MAPS.ip2n.refresh();
        this.MAPS.n2ip.refresh();
    }
    if (instance.CONFIG.params.Resolve_IP_To_User == "yes") {
        this.MAPS.ip2u.refresh();
    }
    for (var key in this.STORE) {
        if (this.STORE[key].retrieve().length >= this.STORE[key].MaxRecs || new Date().getTime() > this.STORE[key].WallTimer) {
            try {
                this.STORE[key].Parser();
            } catch (err) {
                log("Parsing failed in Session", 5, instance.LOG | instance.EVENT);
            }
            this.STORE[key].clear();
        }
    }
    return true;
};