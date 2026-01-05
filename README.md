# kBackupSystem

An efficient, secure, and fully automatic backup system for Minecraft worlds.
Developer: vv0ta3fa9 (ImFriendlyy)

---

## 📌 Main idea

kBackupSystem is a high-performance plugin for automatic and manual backup of Minecraft worlds, designed for minimal server load and complete data preservation.

The plugin allows you to:

- Automatically archive worlds at specified intervals
- Save backups when starting and stopping the server
- Delete old backups older than a specified number of days
- Make manual backups on command
- Flexibly configure the archive storage directory
- Create ZIP archives at high speed with detailed debug logging

---


## 🏆 Why is kBackupSystem better than others?

- Does not block the main thread
- Supports multi-world servers
- Has an auto-deletion system
- Correctly archives empty directories
- Has detailed and accurate debugging
- Structured and easily modifiable code
- Supports backup on startup, shutdown, and on schedule
- Ability to perform manual backups

## 🚀 Plugin advantages
 1. Minimal load on the server

All archiving is performed asynchronously, which eliminates lag and freezes of the main server tick.

 2. Correct archiving of the entire world folder

The plugin saves:

- the root folder of the world
- all files and subfolders
- empty directories
- the correct ZIP structure
- This is important for transfers and emergency recoveries.

---

## 🧠 3. Smart task system

The built-in scheduler allows you to perform backups:

- automatically after N minutes
- when the server starts
- when the server stops

---

## 🧹 4. Automatic deletion of old backups

The plugin automatically clears backups older than the specified number of days.

---

## 📁 5. Two storage modes

- Main-folder — in the server root

- Plugin-folder — in the plugin folder

---

## 🧪 6. Detailed debug logging

When debug: true, the plugin outputs:

- which files are being archived
- start/end time
- archive size and path
- world information
- directories, files, exceptions

---

## 🔧 7. Easy integration and customization

Each method is structured, divided by managers, and called with a single command.

---


## ⚡ 8. High ZIP speed

Optimized buffers (up to 16K) are used, which speeds up the archiving of large worlds.

---


## 🛠 Commands


| Command                  | Description                                      | Permissions               |
|----------------- ---------|-----------------------------------------------|-------------------- -|
| `/kbackupsystem reload`     | Reload plugin                           | `kbackupsystem.admin`   |
| `/kbackupsystem start`      | Force backup start                                 | `kbackupsystem.admin`   |


---


## 📁 Archive structure

Archive example:

world_2025-01-01_14-03-29.zip
└── world/
    ├── level.dat
    ├── region/
    ├── entities/
    ├── data/
    ├── playerdata/
    └── session.lock
