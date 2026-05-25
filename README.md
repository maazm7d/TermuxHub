<h1 align="center">TermuxHub</h1>

<p align="center">
  Android application that indexes Termux tools using metadata.<br/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fraw.githubusercontent.com%2Fmaazm7d%2FTermuxHub%2Fmain%2Fmetadata%2Fmetadata.json&query=%24.tools.length&label=tools&color=blue" alt="Tools Count">
  <img src="https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fraw.githubusercontent.com%2Fmaazm7d%2FTermuxHub%2Fmain%2Fmetadata%2Frepo_stats.json&query=%24.lastUpdated&label=last%20metadata%20update&color=green" alt="Last Metadata Update">
  <a href="https://github.com/maazm7d/TermuxHub/actions/workflows/build.yml">
    <img src="https://github.com/maazm7d/TermuxHub/actions/workflows/build.yml/badge.svg" alt="CI Status">
  </a>
  <a href="https://github.com/maazm7d/TermuxHub/actions/workflows/metadata-sync.yml">
    <img src="https://github.com/maazm7d/TermuxHub/actions/workflows/metadata-sync.yml/badge.svg" alt="Metadata Sync Status">
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-7.1.1%2B-green.svg?logo=android" alt="Android Version">
  <a href="https://github.com/maazm7d/TermuxHub/releases">
    <img src="https://img.shields.io/github/v/release/maazm7d/TermuxHub" alt="Latest Release">
  </a>
  <a href="LICENSE">
    <img src="https://img.shields.io/github/license/maazm7d/TermuxHub" alt="License">
  </a>
  <a href="https://github.com/maazm7d/TermuxHub/stargazers">
    <img src="https://img.shields.io/github/stars/maazm7d/TermuxHub?style=social" alt="GitHub stars">
  </a>
  <a href="https://github.com/maazm7d/TermuxHub/network/members">
    <img src="https://img.shields.io/github/forks/maazm7d/TermuxHub?style=social" alt="GitHub forks">
  </a>
</p>

<p align="center">
  <a href="https://github.com/maazm7d/TermuxHub/releases/latest/download/app-release.apk">
    <img
      src="https://raw.githubusercontent.com/NeoApplications/Neo-Backup/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png"
      alt="Get it on GitHub"
      height="80"
    />
  </a>
  <a href="https://f-droid.org/packages/com.maazm7d.termuxhub/">
    <img
      src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80"
    />
  </a>
</p>

<div align="center">
  <small>
    <em>
      Termux Hub is a read-only index for discovery only and does not host,
      distribute, execute, or endorse any third-party tools; all use is at your
      own risk, and you are solely responsible for reviewing code, licenses,
      security, and legal compliance. This project is provided “AS IS”, with no
      warranties and no liability for any damage, security issues, or legal
      consequences.
    </em>
  </small>
</div>

<h2>Data Sources</h2>

<p>
  All data is <strong>read-only</strong> and sourced from:<br/>
  <a href="https://github.com/maazm7d/TermuxHub">
    https://github.com/maazm7d/TermuxHub
  </a>
</p>

<p>Consumed files:</p>

<h3>Tools</h3>
<ul>
  <li>
    Metadata index<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/metadata.json</code>
  </li>
  <li>
    Star counts<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/stars.json</code>
  </li>
  <li>
    Tool README<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/readme/&lt;tool.id&gt;.md</code>
  </li>
  <li>
    Thumbnails<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/thumbnail/&lt;tool.id&gt;.webp</code>
  </li>
</ul>

<h3>Hall of Fame</h3>
<ul>
  <li>
    Index<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/halloffame/index.json</code>
  </li>
  <li>
    Member markdown<br/>
    <code>https://github.com/maazm7d/TermuxHub/metadata/halloffame/&lt;member.id&gt;.md</code>
  </li>
</ul>

<p>Only these resources are consumed.</p>

<h2>Data Pipeline</h2>

<p>
  For a detailed explanation of how metadata is processed, synchronized via GitHub Actions,
  and delivered to the Android application, see:
  <a href="DATA_PIPELINE.md">DATA_PIPELINE.md</a>
</p>

<h2>Contributing &amp; Indexing Your Own Tool</h2>
<p>
  See:
  <a href="CONTRIBUTING.md">CONTRIBUTING.md</a>
</p>

<h2>Termux Hub Badge</h2>

<p>
  Tool maintainers can add an Open-in Termux Hub badge to their README.
  Clicking the badge opens the tool directly in the TermuxHub app.
</p>

<details>
  <summary><strong>Preview</strong></summary>
  <br/>
  <a href="https://maazm7d.github.io/termuxhub/tool/0001">
    <img
      src="https://raw.githubusercontent.com/maazm7d/TermuxHub/main/assets/open-in.png"
      alt="Open in TermuxHub"
      height="80"
    />
  </a>
</details>

[![Star History Chart](https://api.star-history.com/svg?repos=maazm7d/TermuxHub&type=date&legend=top-left)](https://www.star-history.com/#star-history/star-history&type=date&legend=top-left)
