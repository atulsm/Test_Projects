function File(fileName, mode) {
    this.name = fileName;
    this.eol = java.lang.System.getProperty("line.separator");
    this.readLimit = 1048576;
    if (typeof fileName != "undefined") {
        try {
            if ((mode == "w" || mode == "W") && (fileName.substr(0, instance.CONFIG.esecDir.length + 4) == instance.CONFIG.esecDir + "data" || fileName.substr(0, instance.CONFIG.esecDir.length + 3) == instance.CONFIG.esecDir + "log")) {
                this.output = new java.io.BufferedWriter(new java.io.FileWriter(fileName, true));
            } else {
                this.input = new java.io.BufferedReader(new java.io.FileReader(fileName));
            }
            this.offset = 0;
        } catch (err) {
            log("Unable to open file " + fileName + ". Error:" + err, 5, instance.LOG);
        }
    }
    this.isOpen = function () {
        if (typeof this.input != "undefined" || typeof this.output != "undefined") {
            return true;
        }
        return false;
    };
    this.readLine = function () {
        var outString = "";
        if (typeof this.input != 'undefined' && this.input.ready()) {
            outString = String(this.input.readLine());
            this.offset += ((outString + this.eol).length * 2);
            return outString;
        } else {
            return undefined;
        }
    };
    this.writeLine = function (line, addLnSep) {
        if (typeof this.output != 'undefined' && typeof line != 'undefined') {
            if (addLnSep === false) {
                this.output.write(line);
            } else {
                this.output.write(line + this.eol);
            }
            this.output.flush();
            return true;
        } else {
            return false;
        }
    };
    this.readFile = function () {
        if (typeof this.input == 'undefined' || !this.input.ready()) {
            return undefined;
        }
        var fileString = "";
        var file = java.io.File(this.name);
        if ((file.length() - this.offset) < this.readLimit) {
            for (var inString = this.readLine(); typeof inString != 'undefined'; inString = this.readLine()) {
                fileString += inString + this.eol;
            }
            return fileString;
        } else {
            log("The contents read from the file " + this.name + " exceeds 1MB size limit. Workaround : use readLine() function n times to read n lines.", 5, instance.LOG);
            return undefined;
        }
    };
    this.close = function () {
        if (typeof this.input != 'undefined') {
            this.input.close();
            this.input = undefined;
        }
        if (typeof this.output != 'undefined') {
            this.output.close();
            this.output = undefined;
        }
        this.offset = 0;
    };
}
File.glob = function (dir, glob) {
    if (typeof glob == 'undefined') {
        log(" The file glob expression is not defined.", 5, instance.LOG);
        return undefined;
    }
    var dirPath = new java.io.File(dir);
    if (typeof dir == 'undefined' || !dirPath.exists()) {
        log(" No such file or directory : " + dir + ". ", 5, instance.LOG);
        return undefined;
    }
    if (!dirPath.isDirectory()) {
        log(dir + " is not a directory!", 5, instance.LOG);
        return undefined;
    }
    var fileList = [];
    var fileTokens = dirPath.listFiles();
    var count = 0;
    for (var i = 0; i < fileTokens.length; i++) {
        var _currentFile = fileTokens[i];
        if (_currentFile.isFile()) {
            if (_currentFile.getName().search(glob) != -1) {
                fileList[count] = _currentFile.getAbsolutePath();
                count++;
            }
        }
    }
    log("Found " + count + " files which match '" + glob + "' glob literal", 0, instance.LOG);
    return fileList;
};
File.create = function (file) {
    if (typeof file == "undefined" || (file.substr(0, instance.CONFIG.esecDir.length + 4) != instance.CONFIG.esecDir + "data" && file.substr(0, instance.CONFIG.esecDir.length + 3) != instance.CONFIG.esecDir + "log")) {
        return false;
    }
    var dir = file.substr(0, file.lastIndexOf("/"));
    var success = (new java.io.File(dir)).mkdirs();
    if (!success) {
        return false;
    }
    success = (new java.io.File(file)).createNewFile();
    if (!success) {
        return false;
    }
    return true;
}
File.rename = function (fromName, toName) {
    if (typeof fromName == "undefined" || (fromName.substr(0, instance.CONFIG.esecDir.length + 4) != instance.CONFIG.esecDir + "data" && fromName.substr(0, instance.CONFIG.esecDir.length + 3) != instance.CONFIG.esecDir + "log")) {
        return false;
    }
    var finalFlag = false;
    var flag = FileUtil.renameFile(fromName, toName);
    log("FileUtil.renameFile(..) returns " + flag, 0, instance.LOG);
    if (flag === 0) {
        finalFlag = true;
    } else {
        log("Could not rename the file " + fromName, 5, instance.LOG);
        finalFlag = false;
    }
    return finalFlag;
};
File.remove = function (fileName) {
    if (typeof fileName == "undefined" || (fileName.substr(0, instance.CONFIG.esecDir.length + 4) != instance.CONFIG.esecDir + "data" && fileName.substr(0, instance.CONFIG.esecDir.length + 3) != instance.CONFIG.esecDir + "log")) {
        return false;
    }
    var finalFlag = false;
    var flag = FileUtil.deleteFile(fileName);
    log("FileUtil.deleteFile(..) returns " + flag, 0, instance.LOG);
    if (flag === 0) {
        finalFlag = true;
    } else {
        log("Could not delete the file " + fileName, 5, instance.LOG);
        finalFlag = false;
    }
    return finalFlag;
};
File.modTime = function (fileName) {
    if (typeof fileName == "undefined" || (fileName.substr(0, instance.CONFIG.esecDir.length + 4) != instance.CONFIG.esecDir + "data" && fileName.substr(0, instance.CONFIG.esecDir.length + 3) != instance.CONFIG.esecDir + "log")) {
        return 0;
    }
    var file = new java.io.File(fileName);
    return file.lastModified();
}