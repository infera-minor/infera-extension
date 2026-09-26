# Infera Research Assistant

Infera is a Chrome side-panel extension backed by a Spring Boot API. It can
summarize text selected in the active browser tab and store personal research
notes locally in Chrome.

## Project layout

- `extension/` — the Manifest V3 browser extension. `manifest.json` declares
  its permissions and entry points; `background.js` opens the side panel;
  `sidepanel.html`, `sidepanel.css`, and `sidepanel.js` provide the UI.
- `backend/` — the Spring Boot service that builds prompts and calls the Gemini
  Generate Content API. `mvnw` and `mvnw.cmd` are generated Maven wrappers and
  should not be edited manually.

## Prerequisites

- Java 21 or later
- A Gemini API key in the `GEMINI_KEY` environment variable
- Google Chrome or another Chromium browser with side-panel support

## Run locally

1. Set `GEMINI_KEY` in the environment used to start the backend.
2. In `backend/`, start the service with `mvn spring-boot:run`. It listens on
   `http://localhost:8080`.
3. In Chrome, open `chrome://extensions`, enable **Developer mode**, choose
   **Load unpacked**, and select the `extension/` directory.
4. Select text on a web page, open Infera, and choose **Summarize**.

Notes are saved only in `chrome.storage.local`; they are not sent to the
backend. The selected page text is sent to the local backend, which sends it
to Gemini to produce the summary.

## Configuration

`backend/src/main/resources/application.properties` holds the application name
and the Gemini endpoint. Keep the API key outside source control by supplying
it through `GEMINI_KEY`.

## Troubleshooting

- A `503` response means the backend cannot reach Gemini. Check internet, VPN,
  proxy, and DNS settings, then restart the backend.
- After changing extension files, press the Reload button for the extension on
  `chrome://extensions`.
- If notes previously display `[object Object]`, save the notes again after
  reloading. The extension discards legacy invalid note values automatically.
