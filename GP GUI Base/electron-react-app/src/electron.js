let electron;
let pathModule;

import('electron').then((electronImport) => {
  electron = electronImport;
  // Import path module inside the dynamic import
  return import('path');
}).then((pathImport) => {
  pathModule = pathImport.default;
  // Call a function to create the window or perform other Electron-related tasks
  createElectronWindow();
});

const isDev = true;

function createElectronWindow() {
  if (!electron || !pathModule) {
    // Handle the case where the electron or path module is not properly imported
    console.error('Electron or path module not imported correctly.');
    return;
  }

  const { app, BrowserWindow } = electron;

  let mainWindow;

  function createWindow() {
    mainWindow = new BrowserWindow({
      width: 800,
      height: 600,
      webPreferences: {
        nodeIntegration: true,
      },
    });

    const startURL = isDev
      ? 'http://localhost:3000'
      : `file://${pathModule.join(__dirname, './public/index.html')}`;

    mainWindow.loadURL(startURL);

    mainWindow.on('closed', () => (mainWindow = null));
  }

  app.on('ready', createWindow);

  app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
      app.quit();
    }
  });

  app.on('activate', () => {
    if (mainWindow === null) {
      createWindow();
    }
  });
}
