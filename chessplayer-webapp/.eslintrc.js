/* eslint-disable quote-props */
module.exports = {
  env: {
    browser: true,
    es2021: true,
  },
  extends: [
    'plugin:react/recommended',
    'airbnb',
  ],
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 12,
    sourceType: 'module',
  },
  plugins: [
    'react',
  ],
  rules: {
    "linebreak-style": 0,
    "indent": 0,
    "brace-style": 0,
    "react/jsx-filename-extension": 0,
    "quotes": 0,
    "react/jsx-indent": 0,
    "no-plusplus": 0,
    "no-use-before-define": ["error", { "functions": false, "classes": true, "variables": true }],
    "import/no-extraneous-dependencies": ["error", { "devDependencies": true }],
  },
};
