/**
 * Initializes the side panel, restores valid locally saved notes, and binds
 * UI actions once the extension page is ready.
 */
document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['researchNotes'], function(result) {
        if (typeof result.researchNotes === 'string') {
            document.getElementById('notes').value = result.researchNotes;
        } else if (result.researchNotes !== undefined) {
            // Remove the invalid value saved by older versions, which stored
            // the textarea element instead of the text the user entered.
            chrome.storage.local.remove('researchNotes');
        }
    });

    document.getElementById('summarizeBtn').addEventListener('click', summarizeText);
    document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
});

async function summarizeText() {
    // Reads the active tab's selected text, then asks the local backend to
    // summarize it through the configured Gemini API.
    try {
        const[tab] = await chrome.tabs.query({ active: true, currentWindow: true})
        const[{ result }] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });
        if (!result) {
            showresult('Please select some text first');
            return;
        }

        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json'},
            body: JSON.stringify({ content: result, operation: 'summarize'})
        })
        if (!response.ok) {
            const error = await response.json().catch(() => null);
            throw new Error(error?.detail || error?.message || `API Error: ${response.status}`);
        }
        const text = await response.text();
        showresult(text.replace(/\n/g, '<br>'))
    }catch (error) {
        showresult('Error: '+error.message)
    }
}

async function saveNotes() {
    // Store text rather than the textarea DOM element so Chrome can restore it.
    const notes = document.getElementById('notes').value;
    chrome.storage.local.set({ researchNotes: notes }, function() {
        alert("Notes saved successfully");
    });
}

function showresult(content) {
    // Replaces the previous result with the latest summary or user-facing error.
    document.getElementById('results').innerHTML = `<div class="result-item"><div class="result-content">${content}</div></div>`;
}
