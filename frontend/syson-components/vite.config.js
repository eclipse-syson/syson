import react from '@vitejs/plugin-react';
import { createRequire } from 'node:module';
import path from 'node:path';
import { defineConfig } from 'vite';

const require = createRequire(import.meta.url);
const { peerDependencies = {} } = require('./package.json');
const isExternal = (id) =>
  Object.keys(peerDependencies).some((dependency) => id === dependency || id.startsWith(`${dependency}/`));

export default defineConfig(() => {
  const configuration = {
    plugins: [react()],
    build: {
      minify: false,
      lib: {
        name: 'syson-components',
        entry: path.resolve(__dirname, 'src/index.ts'),
        formats: ['es', 'cjs'],
        fileName: (format) => `syson-components.${format}.js`,
      },
      rollupOptions: {
        external: isExternal,
      },
    },
    test: {
      environment: 'jsdom',
      coverage: {
        reporter: ['text', 'html'],
      },
    },
  };
  return configuration;
});
