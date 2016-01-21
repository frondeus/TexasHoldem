requirejs.config({
    baseUrl: 'lib',
    paths: {
        app: '../app',
        jquery: 'jquery-2.2.0.min'
    },
    text: {
      useXhr: function (url, protocol, hostname, port) {
        return true;
      }
    }
});

requirejs(['app/main']);
