// modules are defined as an array
// [ module function, map of requires ]
//
// map of requires is short require name -> numeric require
//
// anything defined in a previous bundle is accessed via the
// orig method which is the require for previous bundles

// eslint-disable-next-line no-global-assign
parcelRequire = (function (modules, cache, entry, globalName) {
  // Save the require from previous bundle to this closure if any
  var previousRequire = typeof parcelRequire === 'function' && parcelRequire;
  var nodeRequire = typeof require === 'function' && require;

  function newRequire(name, jumped) {
    if (!cache[name]) {
      if (!modules[name]) {
        // if we cannot find the module within our internal map or
        // cache jump to the current global require ie. the last bundle
        // that was added to the page.
        var currentRequire = typeof parcelRequire === 'function' && parcelRequire;
        if (!jumped && currentRequire) {
          return currentRequire(name, true);
        }

        // If there are other bundles on this page the require from the
        // previous one is saved to 'previousRequire'. Repeat this as
        // many times as there are bundles until the module is found or
        // we exhaust the require chain.
        if (previousRequire) {
          return previousRequire(name, true);
        }

        // Try the node require function if it exists.
        if (nodeRequire && typeof name === 'string') {
          return nodeRequire(name);
        }

        var err = new Error('Cannot find module \'' + name + '\'');
        err.code = 'MODULE_NOT_FOUND';
        throw err;
      }

      localRequire.resolve = resolve;
      localRequire.cache = {};

      var module = cache[name] = new newRequire.Module(name);

      modules[name][0].call(module.exports, localRequire, module, module.exports, this);
    }

    return cache[name].exports;

    function localRequire(x){
      return newRequire(localRequire.resolve(x));
    }

    function resolve(x){
      return modules[name][1][x] || x;
    }
  }

  function Module(moduleName) {
    this.id = moduleName;
    this.bundle = newRequire;
    this.exports = {};
  }

  newRequire.isParcelRequire = true;
  newRequire.Module = Module;
  newRequire.modules = modules;
  newRequire.cache = cache;
  newRequire.parent = previousRequire;
  newRequire.register = function (id, exports) {
    modules[id] = [function (require, module) {
      module.exports = exports;
    }, {}];
  };

  for (var i = 0; i < entry.length; i++) {
    newRequire(entry[i]);
  }

  if (entry.length) {
    // Expose entry point to Node, AMD or browser globals
    // Based on https://github.com/ForbesLindesay/umd/blob/master/template.js
    var mainExports = newRequire(entry[entry.length - 1]);

    // CommonJS
    if (typeof exports === "object" && typeof module !== "undefined") {
      module.exports = mainExports;

    // RequireJS
    } else if (typeof define === "function" && define.amd) {
     define(function () {
       return mainExports;
     });

    // <script>
    } else if (globalName) {
      this[globalName] = mainExports;
    }
  }

  // Override the current require with this new one
  return newRequire;
})({"utils.js":[function(require,module,exports) {
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.encodeObj = void 0;

var encodeObj = function encodeObj(obj) {
  //Sacado de acá: https://stackoverflow.com/a/35416293
  return Object.keys(obj).map(function (k) {
    return "".concat(encodeURIComponent(k), "=").concat(encodeURIComponent(obj[k]));
  }).join('&');
};

exports.encodeObj = encodeObj;
},{}],"chat.js":[function(require,module,exports) {
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.onRefreshMessages = exports.onClickMessageSend = exports.getRefreshInterval = void 0;

var _utils = require("./utils");

var REFRESH_INTERVAL = 1000 * 3;

var getRefreshInterval = function getRefreshInterval() {
  return REFRESH_INTERVAL;
};

exports.getRefreshInterval = getRefreshInterval;

var addMessage = function addMessage(_ref) {
  var username = _ref.username,
      message = _ref.message;
  var messagesUl = document.querySelector("#messages-list");
  var newMessage = document.createElement("li");
  newMessage.innerHTML = "<b>".concat(username, "</b>: ").concat(message.message);
  messagesUl.appendChild(newMessage);
};

var cleanInput = function cleanInput() {
  var form = document.querySelector("#message-form");
  var input = form.querySelector("input");
  input.value = "";
};

var getMessage = function getMessage() {
  var form = document.querySelector("#message-form");
  var input = form.querySelector("input");
  return {
    "message": input.value
  };
};

var getMessagesCount = function getMessagesCount() {
  var _length = Array.from(document.querySelector("#messages-list").children).length;
  return _length > 0 ? _length : 0;
};

var onClickMessageSend = function onClickMessageSend(evt) {
  evt.preventDefault();
  console.log("onClickMessageSend");
  var message = getMessage();
  var encodedMessage = (0, _utils.encodeObj)(message);
  console.log({
    encodedMessage: encodedMessage
  });
  fetch("", {
    method: "POST",
    body: encodedMessage,
    headers: {
      //Hay que setear esta cabecera para que el servidor 
      //lo reciba como si le estuviésemos mandando un <form>
      "Content-type": "application/x-www-form-urlencoded"
    }
  }).then(function (res) {
    var username = document.querySelector("#username").innerText;
    addMessage({
      username: username,
      message: message
    });
    cleanInput();
  }).catch(function (err) {
    console.error({
      err: err
    });
  });
};

exports.onClickMessageSend = onClickMessageSend;

var onRefreshMessages = function onRefreshMessages() {
  console.log("onRefreshMessages");
  var msgsCount = getMessagesCount();
  fetch("?starting=".concat(msgsCount)).then(function (res) {
    return res.text();
  }).then(function (res) {
    var parser = new DOMParser();
    var html = parser.parseFromString(res, "text/html");

    try {
      var messages = Array.from(html.querySelector("#messages-list").children);
      messages.forEach(function (msg) {
        document.querySelector("#messages-list").appendChild(msg);
      });
    } catch (e) {//Como la #messages-list puede estar vacía, 
      //va tirar excepción al querer acceder a ".children"
    }
  }).catch(function (err) {
    console.error({
      err: err
    });
  });
};

exports.onRefreshMessages = onRefreshMessages;
},{"./utils":"utils.js"}],"index.js":[function(require,module,exports) {
"use strict";

var _chat = require("./chat");

var setMessageSendHandler = function setMessageSendHandler() {
  document.querySelector("#send-button").addEventListener("click", _chat.onClickMessageSend);
};

var focusMessageForm = function focusMessageForm() {
  var form = document.querySelector("#message-form");
  var input = form.querySelector("input");
  input.focus();
};

window.onload = function () {
  setInterval(_chat.onRefreshMessages, (0, _chat.getRefreshInterval)());
  setMessageSendHandler();
  focusMessageForm();
};
},{"./chat":"chat.js"}],"../node_modules/parcel-bundler/src/builtins/hmr-runtime.js":[function(require,module,exports) {
var global = arguments[3];
var OVERLAY_ID = '__parcel__error__overlay__';
var OldModule = module.bundle.Module;

function Module(moduleName) {
  OldModule.call(this, moduleName);
  this.hot = {
    data: module.bundle.hotData,
    _acceptCallbacks: [],
    _disposeCallbacks: [],
    accept: function (fn) {
      this._acceptCallbacks.push(fn || function () {});
    },
    dispose: function (fn) {
      this._disposeCallbacks.push(fn);
    }
  };
  module.bundle.hotData = null;
}

module.bundle.Module = Module;
var parent = module.bundle.parent;

if ((!parent || !parent.isParcelRequire) && typeof WebSocket !== 'undefined') {
  var hostname = "" || location.hostname;
  var protocol = location.protocol === 'https:' ? 'wss' : 'ws';
  var ws = new WebSocket(protocol + '://' + hostname + ':' + "39775" + '/');

  ws.onmessage = function (event) {
    var data = JSON.parse(event.data);

    if (data.type === 'update') {
      console.clear();
      data.assets.forEach(function (asset) {
        hmrApply(global.parcelRequire, asset);
      });
      data.assets.forEach(function (asset) {
        if (!asset.isNew) {
          hmrAccept(global.parcelRequire, asset.id);
        }
      });
    }

    if (data.type === 'reload') {
      ws.close();

      ws.onclose = function () {
        location.reload();
      };
    }

    if (data.type === 'error-resolved') {
      console.log('[parcel] ✨ Error resolved');
      removeErrorOverlay();
    }

    if (data.type === 'error') {
      console.error('[parcel] 🚨  ' + data.error.message + '\n' + data.error.stack);
      removeErrorOverlay();
      var overlay = createErrorOverlay(data);
      document.body.appendChild(overlay);
    }
  };
}

function removeErrorOverlay() {
  var overlay = document.getElementById(OVERLAY_ID);

  if (overlay) {
    overlay.remove();
  }
}

function createErrorOverlay(data) {
  var overlay = document.createElement('div');
  overlay.id = OVERLAY_ID; // html encode message and stack trace

  var message = document.createElement('div');
  var stackTrace = document.createElement('pre');
  message.innerText = data.error.message;
  stackTrace.innerText = data.error.stack;
  overlay.innerHTML = '<div style="background: black; font-size: 16px; color: white; position: fixed; height: 100%; width: 100%; top: 0px; left: 0px; padding: 30px; opacity: 0.85; font-family: Menlo, Consolas, monospace; z-index: 9999;">' + '<span style="background: red; padding: 2px 4px; border-radius: 2px;">ERROR</span>' + '<span style="top: 2px; margin-left: 5px; position: relative;">🚨</span>' + '<div style="font-size: 18px; font-weight: bold; margin-top: 20px;">' + message.innerHTML + '</div>' + '<pre>' + stackTrace.innerHTML + '</pre>' + '</div>';
  return overlay;
}

function getParents(bundle, id) {
  var modules = bundle.modules;

  if (!modules) {
    return [];
  }

  var parents = [];
  var k, d, dep;

  for (k in modules) {
    for (d in modules[k][1]) {
      dep = modules[k][1][d];

      if (dep === id || Array.isArray(dep) && dep[dep.length - 1] === id) {
        parents.push(k);
      }
    }
  }

  if (bundle.parent) {
    parents = parents.concat(getParents(bundle.parent, id));
  }

  return parents;
}

function hmrApply(bundle, asset) {
  var modules = bundle.modules;

  if (!modules) {
    return;
  }

  if (modules[asset.id] || !bundle.parent) {
    var fn = new Function('require', 'module', 'exports', asset.generated.js);
    asset.isNew = !modules[asset.id];
    modules[asset.id] = [fn, asset.deps];
  } else if (bundle.parent) {
    hmrApply(bundle.parent, asset);
  }
}

function hmrAccept(bundle, id) {
  var modules = bundle.modules;

  if (!modules) {
    return;
  }

  if (!modules[id] && bundle.parent) {
    return hmrAccept(bundle.parent, id);
  }

  var cached = bundle.cache[id];
  bundle.hotData = {};

  if (cached) {
    cached.hot.data = bundle.hotData;
  }

  if (cached && cached.hot && cached.hot._disposeCallbacks.length) {
    cached.hot._disposeCallbacks.forEach(function (cb) {
      cb(bundle.hotData);
    });
  }

  delete bundle.cache[id];
  bundle(id);
  cached = bundle.cache[id];

  if (cached && cached.hot && cached.hot._acceptCallbacks.length) {
    cached.hot._acceptCallbacks.forEach(function (cb) {
      cb();
    });

    return true;
  }

  return getParents(global.parcelRequire, id).some(function (id) {
    return hmrAccept(global.parcelRequire, id);
  });
}
},{}]},{},["../node_modules/parcel-bundler/src/builtins/hmr-runtime.js","index.js"], null)
//# sourceMappingURL=/index.map