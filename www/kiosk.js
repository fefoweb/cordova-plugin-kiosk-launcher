var exec = require("cordova/exec");

var Kiosk = {
  setKioskEnabled: function(enabled) {
    exec(null, null, "Kiosk", "setKioskEnabled", [!!enabled]);
  },

  switchLauncher: function() {
    exec(null, null, "Kiosk", "switchLauncher", []);
  },

  isInKiosk: function(callback) {
    exec(
      function(out) {
        callback(out == "true");
      },
      function(error) {
        alert("Kiosk.isInKiosk failed: " + error);
      },
      "Kiosk",
      "isInKiosk",
      []
    );
  },

  isSetAsLauncher: function(callback) {
    exec(
      function(out) {
        callback(out == "true");
      },
      function(error) {
        alert("Kiosk.isSetAsLauncher failed: " + error);
      },
      "Kiosk",
      "isSetAsLauncher",
      []
    );
  }, 

  setKeysRunning: function (keyCodes) {
    exec(
      null, 
      function (error) {
        alert("Kiosk.setKeysRunning failed: " + error);
      }, 
      "Kiosk", 
      "setKeysRunning", 
      keyCodes);
  }, 

  setFullscreen: function(isFullscreen) {
    exec(null, null, "Kiosk", "setFullscreen", [!!isFullscreen]);
  },

  killKiosk: function () {
    exec(null, null, "Kiosk", "killKiosk", []);
    exec(null, null, "Kiosk", "switchLauncher", []);
  }
};

module.exports = Kiosk;
