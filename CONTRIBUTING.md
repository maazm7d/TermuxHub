<div align="center">

# Contributing to TermuxHub

This contribution guide is **strict, concise, and enforced**.  
Pull requests that do not follow these rules **will be closed without exception**.

</div>


---

## Adding a New Tool

### Step 1: Edit `metadata/metadata.json`

Add your tool entry to the metadata file.

#### Field Rules (Strict)

| Field         | Rule |
|---------------|------|
| `id`          | Unique, immutable, and used everywhere |
| `name`        | Tool name only (no emojis, versions, or extras) |
| `description` | One clear and concise sentence |
| `category`    | Must match **one** allowed category exactly |
| `install`     | Termux-compatible shell commands (`\n` allowed) |
| `thumbnail`   | Leave empty |
| `repo`        | GitHub HTTPS URL only (no trailing slash) |
| `author`      | GitHub username or organization (no URL) |
| `requireRoot` | `true` or `false` |
| `publishedAt` | Format: `DD-MM-YYYY` |

#### Allowed Categories

- OSINT & Recon  
- Web Security  
- Network Security  
- Exploitation Tools  
- Brute Force  
- Social Engineering  
- Android Security  
- Cryptography Tools  
- Privacy & Anonymity  
- Utilities & Dev  
- TUI Apps
- Games

---

### Step 2: Add Thumbnail (Mandatory)

Thumbnail is **required** for every tool.

**Path**
`metadata/thumbnail/<tool.id>.webp`

#### Thumbnail Rules

- Format: **WEBP only**
- Aspect Ratio: **16:9**
- Filename **must exactly match** `tool.id`
- Missing thumbnail = **PR rejected**

---

## Stars and READMEs (Auto-Generated)

The following files are **automatically managed**:

- `metadata/stars.json`  
  - Updated daily at **00:00 UTC**
- `metadata/readme/{tool.id}.md`  
  - Fetched directly from upstream GitHub repositories

**Do NOT edit these files in pull requests.**

---

## Hall of Fame

- Contributors **cannot add themselves**
- Entries are added **only by owners or maintainers**
- Self-add pull requests are **closed immediately**

---

## App Source Contributions

- Small, focused pull requests are welcome
- Every merge commit **must clearly explain**:
  - What changed
  - Why it changed

---

## Commit Message Rules (Enforced)

### Format: 

### Allowed Prefixes

- `metadata.json:` — single metadata file change
- `metadata:` — multiple metadata files
- `{file name}:` — single app source file
- `app:` — application source code changes

**Unclear or incorrect commit titles will result in PR rejection.**

---

## Pull Requests Will Be Closed If

- Metadata format is incorrect
- Thumbnail rules are violated
- Auto-generated files are edited
- Commit messages are unclear
- Changes are unrelated or unexplained

---

> Quality, structure, and clarity matter more than quantity.
