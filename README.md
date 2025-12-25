# Healthcare Management App (Java Swing + MVC) / CSA 2

## How to run (VS Code)
1. Ensure you have a JDK installed.
2. Open this folder in VS Code.
3. Place the provided CSVs in the `data/` folder.
4. Run `src/Main.java`.

## Features required by the brief
- Loads CSV data into in-memory lists (no database)
- MVC structure (model/view/controller)
- Java Swing GUI: view tables, search, add/edit/delete
- Singleton pattern for referral management
- CRUD Operations in CSV files
- Generates output text files for referrals and prescriptions into `output/`

> Note: This project keeps data in memory and in csv files. CRUD operations update the UI, CSV and in-memory store.

## Persisting changes
Use the **Save to CSV** button in the toolbar to write your in-memory changes back to the CSV files in `data/`.

## DEV
Developed by Rameesa Asif
