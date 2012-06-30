// RequireJS Optimizer Build Configuration
({
    // Application Location relative to baseURL
    appDir: "../webapp",

    // Application Base URL
    baseUrl: "app",

    // Output location
    dir: "../../../target/jbwdemo-client",

    // Application main configuration file for require.js
    mainConfigFile: "../webapp/app/config.js",

    // Use standard optimization of CSS removing line breaks
    optimizeCss: "standard",

    // Modules to be optimized
    modules: [
        {
            name: "main",
            include: [
                "jquery",
                "jquerym"
            ]
        }
    ]
})