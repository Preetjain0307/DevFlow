#!/bin/bash
# ================================================================
#           DevFlow Database Initialization & Seeding (Linux/Mac)
# ================================================================

echo "================================================================"
echo "          DevFlow Database Initialization & Seeding"
echo "================================================================"

DB_USER="root"
DB_NAME="devflow_db"

read -s -p "Enter MySQL root password (press Enter for default 'root'): " DB_PASS
echo ""
if [ -z "$DB_PASS" ]; then
    DB_PASS="root"
fi

echo "[*] Importing schema.sql..."
mysql -u "$DB_USER" -p"$DB_PASS" < database/schema.sql
if [ $? -ne 0 ]; then
    echo "[!] ERROR: Failed to execute database/schema.sql."
    exit 1
fi
echo "[OK] Schema imported successfully (26 tables created)."

echo "[*] Importing sample dataset..."
mysql -u "$DB_USER" -p"$DB_PASS" < database/sample_data.sql
if [ $? -ne 0 ]; then
    echo "[!] ERROR: Failed to execute database/sample_data.sql."
    exit 1
fi
echo "[OK] Sample dataset seeded successfully!"

echo "================================================================"
echo "       Database Setup Complete! DevFlow is Ready to Run!"
echo "================================================================"
