<h1 align="center">TermuxHub</h1>

<p align="center">
Android application that indexes Termux tools using metadata.<br/>
</p>

## How It Works

- Fetches static metadata from GitHub
- Resolves tools, stars, and markdown independently
- Persists validated data into local Room database
- Local database is the source of truth after sync
- Falls back to cached or bundled data on failure
- **Stars and tool README update automatically at 00:00 UTC**


## Data Sources

All data is **read-only** and sourced from:
https://github.com/maazm7d/TermuxHub

Consumed files:

### Tools
- Metadata index  
  `https://github.com/maazm7d/TermuxHub/metadata/metadata.json`
- Star counts  
  `https://github.com/maazm7d/TermuxHub/metadata/stars.json`
- Tool README  
  `https://github.com/maazm7d/TermuxHub/metadata/readme/<tool.id>.md`
- Thumbnails  
  `https://github.com/maazm7d/TermuxHub/metadata/thumbnail/<tool.id>.webp`

### Hall of Fame
- Index  
  `https://github.com/maazm7d/TermuxHub/metadata/halloffame/index.json`
- Member markdown  
  `https://github.com/maazm7d/TermuxHub/metadata/halloffame/<member.id>.md`


Only these resources are consumed.

## Contributing & Indexing Your Own Tool

See: [CONTRIBUTING.md](CONTRIBUTING.md)
