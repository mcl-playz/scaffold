import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */
const sidebars: SidebarsConfig = {
  tutorialSidebar: [
      {
        type: 'category',
        label: 'Getting Started',
        items: [
          'getting-started/installation',
          'getting-started/first-command',
          'getting-started/configuration',
        ],
      },
      {
        type: 'category',
        label: 'Annotations',
        items: [
          'annotations/command',
          'annotations/root',
          'annotations/sub',
          'annotations/arg',
          'annotations/permission',
          'annotations/executable-by',
          'annotations/cooldown',
        ],
      },
      {
        type: 'category',
        label: 'API',
        items: [
          'api/scaffoldcommandmanager',
          'api/commandbase',
          'api/commandcontext',
          'api/exception-handler',
        ],
      },
    ],
};

export default sidebars;
