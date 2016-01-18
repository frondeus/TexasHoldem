requirejs.config({
    baseUrl: 'lib',
    paths: {
        app: '../app',
        jquery: 'jquery-2.2.0.min'
    }
});

requirejs(['app/main']);
