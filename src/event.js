function Event(proto) {
    for (var src in proto) {
        if (proto.hasOwnProperty(src)) {
            this[src] = proto[src];
        }
    }
}
Event.prototype.setDeviceEventTime = function (time) {
    if (!(time instanceof Date)) {
        return false;
    }
    this.DeviceEventTime = time;
    return true;
};
Event.prototype.setBeginTime = function (time) {
    if (!(time instanceof Date)) {
        return false;
    }
    this.BeginTime = time;
    return true;
};
Event.prototype.setEndTime = function (time) {
    if (!(time instanceof Date)) {
        return false;
    }
    this.EndTime = time;
    return true;
};
Event.prototype.add2EI = function (name, val) {
    if (typeof name == "undefined" || typeof val == "undefined" || name === "" || val === "") {
        return false;
    }
    this.ExtendedInformation = this.ExtendedInformation ? (this.ExtendedInformation + " " + name + "=" + val + ";") : (name + "=" + val + ";");
    return true;
};
Event.prototype.setTaxKey = function (key) {
    if (typeof key == "undefined") {
        return false;
    }
    var taxonomy = instance.MAPS.tax.lookup(key);
    if (typeof taxonomy == "undefined") {
        return false;
    }
    this.TaxonomyLevel1 = taxonomy[0];
    this.TaxonomyLevel2 = taxonomy[1];
    this.TaxonomyLevel3 = taxonomy[2];
    this.TaxonomyLevel4 = taxonomy[3];
    this.XDASTaxonomyName = taxonomy[4];
    this.XDASOutcomeName = taxonomy[5];
    var taxcode = instance.MAPS.taxcode.lookup(this.XDASTaxonomyName);
    var taxout = instance.MAPS.taxout.lookup(this.XDASOutcomeName);
    this.XDASRegistry = taxcode[0];
    this.XDASProvider = taxcode[1];
    this.XDASClass = taxcode[2];
    this.XDASIdentifier = taxcode[3];
    this.XDASOutcome = taxout[0];
    this.XDASDetail = taxout[1];
    return true;
};
Event.prototype.send = function (datamap) {
    if (typeof datamap == "undefined") {
        datamap = instance.MAPS.Rec2Evt;
    }
    if (typeof rec != "undefined") {
        rec.convert(this, datamap);
    } else {
        rec = new Record();
        var hashMap = new HashMap();
        rec.connectorData = new ConnectorData(hashMap);
    }
    if (instance.CONFIG.params.ExecutionMode == "debug") {
        instance.CONFIG.debugFile.writeLine(JSON.stringify(this) + "\n");
    }
    if (rec.CONNECTION_MODE == "Replay") {
        this.SensorType = "T";
    }
    if (instance.CONFIG.params.Resolve_IP_and_Hostname == "yes") {
        var assets = {
            "Init": null,
            "Target": null,
            "Observer": null,
            "Reporter": null
        };
        var unknownIPs = [];
        var unknownHosts = [];
        var fqdn;
        for (var asset in assets) {
            if (typeof this[asset + "HostName"] == 'undefined') {
                if (typeof this[asset + "IP"] != 'undefined') {
                    if (typeof instance.MAPS.ip2n[this[asset + "IP"]] != 'undefined') {
                        fqdn = instance.MAPS.ip2n[this[asset + "IP"]][0];
                        if (fqdn.search(/\./) != -1) {
                            this[asset + "HostName"] = fqdn.substr(0, fqdn.indexOf("."));
                            this[asset + "HostDomain"] = fqdn.substr(fqdn.indexOf(".") + 1);
                        } else {
                            this[asset + "HostName"] = fqdn;
                        }
                    } else {
                        unknownIPs.push(this[asset + "IP"]);
                    }
                }
            } else {
                if (typeof this[asset + "IP"] == 'undefined') {
                    if (typeof this[asset + "HostName"] != 'undefined' && typeof this[asset + "HostDomain"] != 'undefined') {
                        fqdn = this[asset + "HostName"] + "." + this[asset + "HostDomain"];
                    } else if (typeof this[asset + "HostName"] != 'undefined' && typeof this[asset + "HostDomain"] == 'undefined') {
                        fqdn = this[asset + "HostName"];
                    }
                    if (typeof fqdn != 'undefined') {
                        if (typeof instance.MAPS.n2ip[fqdn] != 'undefined') {
                            this[asset + "IP"] = instance.MAPS.n2ip[fqdn][0];
                        } else {
                            unknownHosts.push(fqdn);
                        }
                    }
                }
            }
        }
        if (unknownIPs.length > 0) {
            var needIPsFile = new File(instance.CONFIG.commonDir + instance.UUID + ".needIPs.tmp", "w");
            for (var i = unknownIPs.length - 1; i > -1; i--) {
                needIPsFile.writeLine(unknownIPs[i]);
            }
            needIPsFile.close();
        }
        if (unknownHosts.length > 0) {
            var needHostsFile = new File(instance.CONFIG.commonDir + instance.UUID + ".needHosts.tmp", "w");
            for (i = unknownHosts.length - 1; i > -1; i--) {
                needHostsFile.writeLine(unknownHosts[i]);
            }
            needHostsFile.close();
        }
    }
    if (instance.CONFIG.params.Resolve_IP_To_User == "yes") {
        if (this.XDASClass == "2" && this.XDASIdentifier == "0" && this.XDASOutcome == "0" && (typeof this.TargetUserName != "undefined" || this.TargetUserName != "") && (typeof this.TargetUserID != "undefined" || this.TargetUserID != "") && typeof this.TargetIP != "undefined" && this.TargetIP != "") {
            var ip2uFile = new File(instance.CONFIG.commonDir + instance.UUID + ".ip2u.tmp", "w");
            ip2uFile.writeLine(this.TargetIP + "," + this.TargetUserName + "," + this.TargetUserDomain + "," + this.TargetUserID);
            ip2uFile.close();
        }
        if (typeof this.InitUserName == "undefined" && typeof this.InitUserID == "undefined" && typeof this.InitIP != undefined && this.InitIP != "") {
            var userinfo = instance.MAPS.ip2u.lookup[this.InitIP];
            if (typeof userinfo[0] != "undefined" && userinfo[0] != "") {
                this.InitUserName = userinfo[0];
            }
            if (typeof userinfo[1] != "undefined" && userinfo[1] != "") {
                this.InitUserDomain = userinfo[1];
            }
            if (typeof userinfo[2] != "undefined" && userinfo[2] != "") {
                this.InitUserID = userinfo[2];
            }
        }
    }
    var JEvent = new EventData();
    for (var field in this) {
        if (this.hasOwnProperty(field)) {
            var value = this[field];
            if (value != "" && instance.MAPS.Evt2EvtData[field] && typeof value != "undefined" && value != "undefined") {
                JEvent.setString(instance.MAPS.Evt2EvtData[field], String(value));
            }
        }
    }
    if (this.DeviceEventTime instanceof Date) {
        this.DeviceEventTime.adjustTimezone();
        JEvent.setDeviceEventTime(String(this.DeviceEventTime.getTime()));
    }
    if (this.BeginTime instanceof Date) {
        this.BeginTime.adjustTimezone();
        JEvent.setString("bgnt", String(this.BeginTime.getTime()));
    }
    if (this.EndTime instanceof Date) {
        this.EndTime.adjustTimezone();
        JEvent.setString("endt", String(this.EndTime.getTime()));
    }
    instance.CONFIG.scriptContext.fireEvent(JEvent, rec.connectorData);
    return true;
};