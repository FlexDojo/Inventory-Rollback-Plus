package me.danjono.inventoryrollback.data;

import com.nuclyon.technicallycoded.inventoryrollback.InventoryRollbackPlus;
import me.danjono.inventoryrollback.InventoryRollback;
import me.danjono.inventoryrollback.config.ConfigData;
import me.danjono.inventoryrollback.config.MessageData;
import me.danjono.inventoryrollback.gui.InventoryName;
import me.danjono.inventoryrollback.inventory.RestoreInventory;
import me.danjono.inventoryrollback.inventory.SaveInventory;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQL {

    private final UUID uuid;
    private final Long timestamp;
    private final BackupTable backupTable;

    public enum BackupTable {
        DEATH(ConfigData.getMySQLTablePrefix() + "deaths"),
        JOIN(ConfigData.getMySQLTablePrefix() + "joins"),
        QUIT(ConfigData.getMySQLTablePrefix() + "quits"),
        WORLD_CHANGE(ConfigData.getMySQLTablePrefix() + "world_changes"),
        FORCE(ConfigData.getMySQLTablePrefix() + "force_backups");

        private final String tableName;

        BackupTable(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return this.tableName;
        }
    }

    private Connection connection;

    private String mainInventory;
    private String armour;
    private String enderChest;
    private String world;
    private double x;
    private double y;
    private double z;
    private final LogType logType;
    private String packageVersion;
    private String deathReason;

    public MySQL(UUID uuid, LogType logType, Long timestamp) {
        this.uuid = uuid;
        this.logType = logType;
        this.timestamp = timestamp;
        this.backupTable = getBackupTable();
    }

    public void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            connection = DriverManager.getConnection("jdbc:mysql://" + 
                    ConfigData.getMySQLHost() + ":" + 
                    ConfigData.getMySQLPort() + "/" + 
                    ConfigData.getMySQLDatabase() + 
                    "?connectionTimeout=30000" + 
                    "&socketTimeout=45000" +
                    "&useSSL=" + 
                    ConfigData.isMySQLUseSSL() +
                    "&verifyServerCertificate=" +
                    ConfigData.isMySQLVerifyCertificate() +
                    "&allowPublicKeyRetrieval=" +
                    ConfigData.isMySQLPubKeyRetrievalAllowed() +
                    "&characterEncoding=UTF-8",
                    ConfigData.getMySQLUsername(),
                    ConfigData.getMySQLPassword());
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void createTables() throws SQLException {
        openConnection();
        
        try {
            for (BackupTable table : BackupTable.values()) {
                String tableQuery = "CREATE TABLE IF NOT EXISTS " + table.getTableName() + " (" +
                        "`id` INT NOT NULL AUTO_INCREMENT," +
                        "`uuid` VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," +
                        "`timestamp` DOUBLE NOT NULL," +
                        "`location_world` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," +
                        "`location_x` DOUBLE NOT NULL," + 
                        "`location_y` DOUBLE NOT NULL," + 
                        "`location_z` DOUBLE NOT NULL," + 
                        "`version` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL," + 
                        "`death_reason` TEXT CHARACTER SET utf8 COLLATE utf8_general_ci," + 
                        "`main_inventory` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci," + 
                        "`armour` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci," + 
                        "`ender_chest` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci," +
                        "PRIMARY KEY (`id`));";
                
                try (PreparedStatement statement = connection.prepareStatement(tableQuery)) {
                    statement.executeUpdate();
                }
            }
        } finally {
            closeConnection();
        }
    }

    private BackupTable getBackupTable() {          
        if (logType == LogType.JOIN) {
            return BackupTable.JOIN;
        } else if (logType == LogType.QUIT) {
            return BackupTable.QUIT;
        } else if (logType == LogType.DEATH) {
            return BackupTable.DEATH;
        } else if (logType == LogType.WORLD_CHANGE) {
            return BackupTable.WORLD_CHANGE;
        } else if (logType == LogType.FORCE) {
            return BackupTable.FORCE;
        }

        return null;
    }

    public boolean doesBackupTypeExist() throws SQLException {        
        openConnection();

        try {
            String query = "SELECT EXISTS(SELECT 1 FROM " + backupTable.getTableName() + " WHERE uuid = ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid + "");
                
                try (ResultSet results = statement.executeQuery()) {
                    results.next();
                    return results.getBoolean(1);
                }
            }

        } finally {
            closeConnection();
        }
    }

    public int getAmountOfBackups() throws SQLException {
        openConnection();

        try {
            String query = "SELECT COUNT(id) FROM " + backupTable.getTableName() + " WHERE uuid = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid + "");
                
                try (ResultSet results = statement.executeQuery()) {
                    results.next();
                    return results.getInt(1);
                }
            }
        } finally {
            closeConnection();
        }
    }

    public List<Long> getSelectedPageTimestamps(int pageNumber) throws SQLException {
        openConnection();
        
        List<Long> timeStamps = new ArrayList<>();

        //Number of backups that will be on the page
        int backups = InventoryName.ROLLBACK_LIST.getSize() - 9;
                
        try {
            int queryRange = ((pageNumber - 1) * backups);
            if (queryRange < 0)
                queryRange = 0;

            String query = "SELECT timestamp FROM " + backupTable.getTableName() + " WHERE uuid = ? ORDER BY timestamp DESC LIMIT " + queryRange + ", " + backups + "";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid + "");
                
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        timeStamps.add(results.getLong(1));
                    }
                }
            }

            return timeStamps;
        } finally {
            closeConnection();
        }
    }

    public void purgeExcessSaves(int deleteAmount) throws SQLException {
        openConnection();

        try {
            String delete = "DELETE FROM " + backupTable.getTableName() + " WHERE uuid = ? ORDER BY timestamp ASC LIMIT " + deleteAmount;
            
            try (PreparedStatement statement = connection.prepareStatement(delete)) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            }
        } finally {
            closeConnection();
        }
    }

    public void setMainInventory(ItemStack[] items) {
        this.mainInventory = SaveInventory.toBase64(items);
    }

    public void setArmour(ItemStack[] items) {
        this.armour = SaveInventory.toBase64(items);
    }

    public void setEnderChest(ItemStack[] items) {
        this.enderChest = SaveInventory.toBase64(items);
    }


    public void setWorld(String world) {
        this.world = world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public void getRollbackMenuData() throws SQLException {
        openConnection();

        try {
            String query = "SELECT timestamp,death_reason,location_world,location_x,location_y,location_z " + 
                    "FROM " + backupTable.getTableName() + " WHERE " +
                    "uuid = ? AND timestamp = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid + "");
                statement.setLong(2, timestamp);
                
                try (ResultSet results = statement.executeQuery()) {
                    results.next();
                    
                    world = results.getString("location_world");
                    x = results.getDouble("location_x");
                    y = results.getDouble("location_y");
                    z = results.getDouble("location_z");
                    deathReason = results.getString("death_reason");
                }
            }
        } finally {
            closeConnection();
        }
    }

    public void getAllBackupData() throws SQLException {
        openConnection();

        try {
            String query = "SELECT * FROM " + backupTable.getTableName() + " WHERE " +
                    "uuid = ? AND timestamp = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, uuid + "");
                statement.setLong(2, timestamp);
                
                try (ResultSet results = statement.executeQuery()) {
                    results.next();
        
                    mainInventory = results.getString("main_inventory");
                    armour = results.getString("armour");
                    enderChest = results.getString("ender_chest");
                    
                    world = results.getString("location_world");
                    x = results.getDouble("location_x");
                    y = results.getDouble("location_y");
                    z = results.getDouble("location_z");
                    
                    packageVersion = results.getString("version");
                    deathReason = results.getString("death_reason");
                }
            }
        } finally {
            closeConnection();
        }
    }

    public ItemStack[] getMainInventory() {
        return RestoreInventory.getInventoryItems(packageVersion, mainInventory);
    }

    public ItemStack[] getArmour() {
        return RestoreInventory.getInventoryItems(packageVersion, armour);
    }

    public ItemStack[] getEnderChest() {
        return RestoreInventory.getInventoryItems(packageVersion, enderChest);
    }

    public String getWorld() {
        return this.world;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public String getVersion() {
        return this.packageVersion;
    }

    public String getDeathReason() {
        return this.deathReason;
    }

    public void saveData() throws SQLException {
        openConnection();

        try {
            String update = "INSERT INTO " + backupTable.getTableName() + " " +
                    "(uuid, timestamp, location_world, location_x, location_y, location_z, version, death_reason, main_inventory, armour, ender_chest)" + " " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(update)) {
                statement.setString(1, uuid + "");
                statement.setLong(2, timestamp);
                statement.setString(3, world);
                statement.setDouble(4, x);
                statement.setDouble(5, y);
                statement.setDouble(6, z);
                statement.setString(7, packageVersion);
                statement.setString(8, deathReason);
                statement.setString(9, mainInventory);
                statement.setString(10, armour);
                statement.setString(11, enderChest);
                statement.executeUpdate();
            }
        } finally {
            closeConnection();
        }
    }

    public static void convertYAMLToMySQL() {
        List<File> backupLocations = new ArrayList<>();
        File backupArea = new File(ConfigData.getFolderLocation().getAbsoluteFile(), YAML.getBackupFolderName());
        
        backupLocations.add(new File(backupArea, "deaths"));
        backupLocations.add(new File(backupArea, "joins"));
        backupLocations.add(new File(backupArea, "quits"));
        backupLocations.add(new File(backupArea, "worldChanges"));
        backupLocations.add(new File(backupArea, "force"));

        List<LogType> logTypeFiles = new ArrayList<>();
        int logTypeNumber = 0;
        logTypeFiles.add(LogType.DEATH);
        logTypeFiles.add(LogType.JOIN);
        logTypeFiles.add(LogType.QUIT);
        logTypeFiles.add(LogType.WORLD_CHANGE);
        logTypeFiles.add(LogType.FORCE);

        for (File backupType : backupLocations) {
            LogType logType = logTypeFiles.get(logTypeNumber);
            InventoryRollbackPlus.getInstance().getConsoleSender().sendMessage(MessageData.getPluginPrefix() + "Converting the backup location " + logType.name());

            if (backupType == null) continue;

            File[] uuidBackups = backupType.listFiles();
            if (uuidBackups == null) continue;

            for (File UUIDBackup : uuidBackups) {
                UUID uuid = UUID.fromString(UUIDBackup.getName());

                File[] backups = UUIDBackup.listFiles();
                if (backups == null) continue;

                for (File backup : backups) {
                    int pos = backup.getName().lastIndexOf(".");
                    String fileName = backup.getName().substring(0, pos);

                    if (!StringUtils.isNumeric(fileName))
                        continue;

                    Long timestamp = Long.parseLong(fileName);

                    YAML yaml = new YAML(uuid, logType, timestamp);
                    MySQL mysql = new MySQL(uuid, logType, timestamp);

                    mysql.setMainInventory(yaml.getMainInventory());
                    mysql.setArmour(yaml.getArmour());
                    mysql.setEnderChest(yaml.getEnderChest());
                    mysql.setWorld(yaml.getWorld());
                    mysql.setX(yaml.getX());
                    mysql.setY(yaml.getY());
                    mysql.setZ(yaml.getZ());
                    mysql.setVersion(yaml.getVersion());
                    mysql.setDeathReason(yaml.getDeathReason());

                    try {
                        mysql.saveData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } 
            }

            logTypeNumber++;

        }

        InventoryRollback.getPluginLogger().info(MessageData.getPluginPrefix() + "Conversion completed!");

    }

}
