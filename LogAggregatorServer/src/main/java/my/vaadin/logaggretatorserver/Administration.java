package my.vaadin.logaggretatorserver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Administration {

    public User user;
    public Application application;
    public Company company;
    public UserGroup user_group;
    public ObjectCollections object_collections;

    private final ServerDataBase database_connection = new ServerDataBase();
    
    /**
     * Constructs the Administration object.
     * 
     * @param user_groups UserGroups object
     * @param user_company CompanyRow object
     */
    public Administration(UserGroups user_groups, CompanyRow user_company) {
        
        this.user = new User(user_groups, user_company);
        this.application = new Application(user_groups, user_company);
        this.company = new Company(user_groups, user_company);
        this.user_group = new UserGroup(user_groups);
        this.object_collections = new ObjectCollections(user_groups, user_company);
    }
    
    /**
     * Constructs a Administration object with zero administration rights.
     */
    public Administration() {
        this(new UserGroups(), new CompanyRow());
    }
    
    //Administration.User object start.
    public class User {
        
        /**
         * The settings related to the Administration.User object.
         */
        public final Settings settings;
        private final UserGroups user_groups;
        private final CompanyRow user_company;
        
        public User(UserGroups user_groups, CompanyRow user_company) {
            
            this.settings = new Settings();
            this.user_groups = user_groups;
            this.user_company = user_company;
        }
        
        //User settings start.
        public class Settings {
            
            public final String ADMINISTRATOR_ID = "1";
            
            public final int FIRST_NAME_MAX_LENGTH = 100;
            public final int LAST_NAME_MAX_LENGTH = 100;
            public final int EMAIL_MAX_LENGTH = 100;
            public final int USERNAME_MAX_LENGTH = 20;
            public final int PASSWORD_MAX_LENGTH = 200;
            
            public String COMPANY_ERROR_MESSAGE = null;
            public String USER_GROUP_ERROR_MESSAGE = null;
            public String FIRST_NAME_ERROR_MESSAGE = null;
            public String LAST_NAME_ERROR_MESSAGE = null;
            public String EMAIL_ERROR_MESSAGE = null;
            public String USERNAME_ERROR_MESSAGE = null;
            public String PASSWORD_ERROR_MESSAGE = null;
            
            /**
             * This method set all error messages back to null.
             */
            public void clear_error_messages() {
                if (this.FIRST_NAME_ERROR_MESSAGE != null) this.FIRST_NAME_ERROR_MESSAGE = null;
                if (this.LAST_NAME_ERROR_MESSAGE != null) this.LAST_NAME_ERROR_MESSAGE = null;
                if (this.EMAIL_ERROR_MESSAGE != null) this.EMAIL_ERROR_MESSAGE = null;
                if (this.USERNAME_ERROR_MESSAGE != null) this.USERNAME_ERROR_MESSAGE = null;
                if (this.PASSWORD_ERROR_MESSAGE != null) this.PASSWORD_ERROR_MESSAGE = null;
                if (this.COMPANY_ERROR_MESSAGE != null) this.COMPANY_ERROR_MESSAGE = null;
                if (this.USER_GROUP_ERROR_MESSAGE != null) this.USER_GROUP_ERROR_MESSAGE = null;
            }
        }
        //User settings end.
        
        //Create user method start.
        /**
         * Creates a user in the user table.
         * 
         * @param first_name The user's first name.
         * @param last_name The user's surname.
         * @param email The user's email address.
         * @param username The user's desired username.
         * @param password The user's desired password.
         * @param company_id The id for the user's company.
         * @param user_group_id The id for the user's user group.
         * @return true if user was created, false if there was an issue.
         */
        public boolean create(String first_name, String last_name, String email, String username, String password, String company_id, String user_group_id) {
            if (this.user_groups.manage_users &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(company_id))) {
                
                boolean proceed = true;
                database_connection.connect();
                
                if (first_name == null || first_name.length() < 1) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (first_name.length() > this.settings.FIRST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your first name can't be longer than " + this.settings.FIRST_NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (last_name == null || last_name.length() < 1) {
                    this.settings.LAST_NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (last_name.length() > this.settings.LAST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your last name can't be longer than " + this.settings.LAST_NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (email == null || email.length() < 1) {
                    this.settings.EMAIL_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (email.length() > this.settings.EMAIL_MAX_LENGTH) {
                    this.settings.EMAIL_ERROR_MESSAGE = "Your email can't be longer than " + this.settings.EMAIL_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (username == null || username.length() < 1) {
                    this.settings.USERNAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (username.length() > this.settings.USERNAME_MAX_LENGTH) {
                    this.settings.USERNAME_ERROR_MESSAGE = "Your username can't be longer than " + this.settings.USERNAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                } else {
                    String[] columnQuery = { "id" };
                    
                    HashMap whereQuery = new HashMap();
                    whereQuery.put("username", username);

                    String[][] select = database_connection.select(columnQuery, "user", whereQuery);

                    if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                        this.settings.USERNAME_ERROR_MESSAGE = "This username is taken.";
                        if (proceed) proceed = false;
                    }
                }

                if (password == null || password.length() < 1) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (password.length() > this.settings.PASSWORD_MAX_LENGTH) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "Your password can't be longer than " + this.settings.PASSWORD_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (company_id == null || company_id.length() < 1) {
                    this.settings.COMPANY_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", company_id);

                    String[][] select = database_connection.select(columnQuery, "company", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.COMPANY_ERROR_MESSAGE = "This company doesn't exist.";
                        if (proceed) proceed = false;
                    }
                }

                if (user_group_id == null || user_group_id.length() < 1) {
                    this.settings.USER_GROUP_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", user_group_id);

                    String[][] select = database_connection.select(columnQuery, "user_groups", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.USER_GROUP_ERROR_MESSAGE = "This user group doesn't exist.";
                        if (proceed) proceed = false;
                    }
                }

                if (proceed) {
                    String[] columnQuery = new String[] {
                                "first_name",
                                "last_name",
                                "email",
                                "username",
                                "password",
                                "company_id",
                                "user_group_id"
                    };

                    String[][] values = {
                        {
                                first_name,
                                last_name,
                                email,
                                username,
                                DataManaging.hashString(password),
                                company_id,
                                user_group_id
                        }
                    };

                    database_connection.insert(columnQuery, values, "user");
                    database_connection.close();
                    
                    //Return true if user was successfully created.
                    return true;
                }

                database_connection.close();
            }
            //Return false if some error was thrown.
            return false;
        }
        //Create user method end.
        
        //Edit user method start.
        /**
         * Change a existing user's information.
         * The provided user's id can't belong to the main administrator.
         * 
         * @param user_information CurrentUser object with the information related to the user subjected to change.
         * @param first_name The new first name of the user subject to change.
         * @param last_name The new surname of the user subject to change.
         * @param email The new email of the user subject to change.
         * @param username The new username of the user subject to change.
         * @param password The new password for the user subject to change.
         * @param company_id The new company id for the user subject to change.
         * @param user_group_id The new user group id for the user subject to change.
         * @return true if the user information was updated, false if there was an issue.
         */
        public boolean edit(CurrentUser user_information, String first_name, String last_name, String email, String username, String password, String company_id, String user_group_id) {
            if (this.user_groups.manage_users && !this.settings.ADMINISTRATOR_ID.equals(user_information.id)  &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(user_information.company.id))) {
                
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                database_connection.connect();
                
                if (first_name == null || first_name.length() < 1) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "This field is required!";
                } else if (first_name.length() > this.settings.FIRST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your first name can't be longer than " + this.settings.FIRST_NAME_MAX_LENGTH + " characters!";
                } else if (!first_name.equals(user_information.first_name)) {
                    columns.add("first_name");
                    values.add(first_name);
                }

                if (last_name == null || last_name.length() < 1) {
                    this.settings.LAST_NAME_ERROR_MESSAGE = "This field is required!";
                } else if (last_name.length() > this.settings.LAST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your last name can't be longer than " + this.settings.LAST_NAME_MAX_LENGTH + " characters!";
                } else if (!last_name.equals(user_information.last_name)) {
                    columns.add("last_name");
                    values.add(last_name);
                }

                if (email == null || email.length() < 1) {
                    this.settings.EMAIL_ERROR_MESSAGE = "This field is required!";
                } else if (email.length() > this.settings.EMAIL_MAX_LENGTH) {
                    this.settings.EMAIL_ERROR_MESSAGE = "Your email can't be longer than " + this.settings.EMAIL_MAX_LENGTH + " characters!";
                } else if (!email.equals(user_information.email)) {
                    columns.add("email");
                    values.add(email);
                }

                if (username == null || username.length() < 1) {
                    this.settings.USERNAME_ERROR_MESSAGE = "This field is required!";
                } else if (username.length() > this.settings.USERNAME_MAX_LENGTH) {
                    this.settings.USERNAME_ERROR_MESSAGE = "Your username can't be longer than " + this.settings.USERNAME_MAX_LENGTH + " characters!";
                } else {
                    if (!username.equals(user_information.username)) {
                        String[] columnQuery = { "id" };

                        HashMap whereQuery = new HashMap();
                        whereQuery.put("username", username);

                        String[][] select = database_connection.select(columnQuery, "user", whereQuery);

                        if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                            this.settings.USERNAME_ERROR_MESSAGE = "This username is taken.";
                        } else {
                            columns.add("username");
                            values.add(username);
                        }
                    }
                }

                if (password == null || password.length() < 1) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "This field is required!";
                } else if (password.length() > this.settings.PASSWORD_MAX_LENGTH) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "Your password can't be longer than " + this.settings.PASSWORD_MAX_LENGTH + " characters!";
                } else {
                    columns.add("password");
                    values.add(password);
                }

                if (company_id == null || company_id.length() < 1) {
                    this.settings.COMPANY_ERROR_MESSAGE = "This field is required!";
                } else {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", company_id);

                    String[][] select = database_connection.select(columnQuery, "company", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.COMPANY_ERROR_MESSAGE = "This company doesn't exist.";
                    } else if (!company_id.equals(user_information.company.id)) {
                        columns.add("company_id");
                        values.add(company_id);
                    }
                }

                if (user_group_id == null || user_group_id.length() < 1) {
                    this.settings.USER_GROUP_ERROR_MESSAGE = "This field is required!";
                } else {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", user_group_id);

                    String[][] select = database_connection.select(columnQuery, "user_groups", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.USER_GROUP_ERROR_MESSAGE = "This user group doesn't exist.";
                    } else if (!user_group_id.equals(user_information.user_group.id)) {
                        columns.add("user_group_id");
                        values.add(user_group_id);
                    }
                }

                if (!columns.isEmpty() && !values.isEmpty()) {

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", user_information.id);
                    
                    database_connection.update(
                            columns.toArray(new String[columns.size()]),
                            values.toArray(),
                            "user",
                            whereQuery
                    );
                    database_connection.close();
                    
                    //Return true if user was updated.
                    return true;
                }

                database_connection.close();
            }
            //Return false if some error was thrown.
            return false;
        }
        //Edit user method end.
        
        //Remove user method start.
        /**
         * Removes the user related to the provided id.
         * The provided user's id can't belong to the main administrator.
         * 
         * @param user_information CurrentUser object representing the user being removed.
         * @return true if the user was removed or doesn't exist, false if there was an issue.
         */
        public boolean remove(CurrentUser user_information) {
            if (this.user_groups.manage_users && user_information != null && !this.settings.ADMINISTRATOR_ID.equals(user_information.id) &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(user_information.company.id))) {

                HashMap whereQuery = new HashMap();
                whereQuery.put("id", user_information.id);
                
                database_connection.connect();
                database_connection.delete_row("user", whereQuery);
                database_connection.close();
                
                //Return true if user was successfully deleted.
                return true;
            }
            //Return false if some error was thrown.
            return false;
        }
        //Remove user method end.
    }
    //Administration.User object end.
    
    //Administration.Application object start.
    public class Application {
        
        /**
         * The settings related to the Administration.Application object.
         */
        public final Settings settings;
        private final UserGroups user_groups;
        private final CompanyRow user_company;
        
        public Application(UserGroups user_groups, CompanyRow user_company) {
            
            this.settings = new Settings();
            this.user_groups = user_groups;
            this.user_company = user_company;
        }
        
        //Application settings start.
        public class Settings {
            
            public final int NAME_MAX_LENGTH = 45;
            public final int LOG_TYPE_MAX_LENGTH = 45;
            
            public String COMPANY_ERROR_MESSAGE = null;
            public String NAME_ERROR_MESSAGE = null;
            public String LOG_TYPE_ERROR_MESSAGE = null;
            public String UPDATE_INTERVAL_ERROR_MESSAGE = null;
            
            /**
             * This method set all error messages back to null.
             */
            public void clear_error_messages() {
                if (this.COMPANY_ERROR_MESSAGE != null) this.COMPANY_ERROR_MESSAGE = null;
                if (this.NAME_ERROR_MESSAGE != null) this.NAME_ERROR_MESSAGE = null;
                if (this.LOG_TYPE_ERROR_MESSAGE != null) this.LOG_TYPE_ERROR_MESSAGE = null;
                if (this.UPDATE_INTERVAL_ERROR_MESSAGE != null) this.UPDATE_INTERVAL_ERROR_MESSAGE = null;
            }
        }
        //Application settings end.
        
        //Create application method start.
        /**
         * Creates a new application in the application table.
         * 
         * @param company_id The id related to the company that this new application will belong to.
         * @param name The name for the new application.
         * @param log_type What log type this application has - can be null.
         * @param update_interval The amount of seconds the interval will be in between each log update.
         * @return An ApplicationRow object containing the information for the application created, returns null if there was an issue.
         */
        public ApplicationRow create(String company_id, String name, String log_type, String update_interval) {
            if (user_groups.manage_applications &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(company_id))) {
                
                boolean proceed = true;
                database_connection.connect();
                
                if (company_id == null || company_id.length() < 1) {
                    this.settings.COMPANY_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", company_id);

                    String[][] select = database_connection.select(columnQuery, "company", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.COMPANY_ERROR_MESSAGE = "This company doesn't exist.";
                        if (proceed) proceed = false;
                    }
                }
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The application name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }
                
                if (log_type != null && log_type.length() > this.settings.LOG_TYPE_MAX_LENGTH) {
                    this.settings.LOG_TYPE_ERROR_MESSAGE = "The log type field can't be longer than " + this.settings.LOG_TYPE_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }
                
                if (update_interval == null || update_interval.length() < 1) {
                    this.settings.UPDATE_INTERVAL_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else {
                    try { Integer.parseInt(update_interval); }
                    catch (NumberFormatException e) {
                        this.settings.UPDATE_INTERVAL_ERROR_MESSAGE = "Incorrect value format!";
                        if (proceed) proceed = false;
                    }
                }
                
                if (proceed) {
                    String[] columnQuery = new String[] {
                                "company_id",
                                "name",
                                "update_interval",
                                "log_type"
                    };

                    Object[] values = {
                                company_id,
                                name,
                                update_interval,
                                log_type
                    };

                    String created_id = database_connection.create(columnQuery, values, "application");
                    
                    if (created_id != null) {
                        String api_key = DataManaging.generateAPIKey(created_id);
                        
                        columnQuery = new String[] { "api_key" };
                        values = new Object[] { api_key };

                        HashMap whereQuery = new HashMap();
                        whereQuery.put("id", created_id);
                        
                        database_connection.update(columnQuery, values, "application", whereQuery);
                        database_connection.close();
                        return new ApplicationRow(created_id, false);
                    }
                    
                    database_connection.close();
                }
            }
            return null;
        }
        //Create application method end.
        
        //Edit application method start.
        /**
         * Update an existing application's information.
         * 
         * @param application_information ApplicationRow object containing the information related to the application being subject to change.
         * @param company_id The id of the new company this application will belong to.
         * @param name The new name for this application.
         * @param log_type The new log type for this application - can be null.
         * @param update_interval The new update interval for this application.
         * @return true if application was updated, false if there was an issue.
         */
        public boolean edit(ApplicationRow application_information, String company_id, String name, String log_type, String update_interval) {
            if (this.user_groups.manage_applications &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(application_information.company.id))) {
                
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                database_connection.connect();
                
                if (company_id == null || company_id.length() < 1) {
                    this.settings.COMPANY_ERROR_MESSAGE = "This field is required!";
                } else if (!company_id.equals(application_information.id)) {
                    String[] columnQuery = { "id" };

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", company_id);

                    String[][] select = database_connection.select(columnQuery, "company", whereQuery);

                    if (select == null || select.length == 0) {
                        this.settings.COMPANY_ERROR_MESSAGE = "This company doesn't exist.";
                    } else {
                        columns.add("company_id");
                        values.add(company_id);
                    }
                }
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The application name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                } else if (!name.equals(application_information.name)) {
                    columns.add("name");
                    values.add(name);
                }

                if (log_type != null && log_type.length() >= 1) {
                    if (log_type.length() > this.settings.LOG_TYPE_MAX_LENGTH) {
                        this.settings.LOG_TYPE_ERROR_MESSAGE = "The log type field can't be longer than " + this.settings.LOG_TYPE_MAX_LENGTH + " characters!";
                    } else if (!log_type.equals(application_information.log_type)) {
                        columns.add("log_type");
                        values.add(log_type);
                    }
                }

                if (update_interval == null || update_interval.length() < 1) {
                    this.settings.UPDATE_INTERVAL_ERROR_MESSAGE = "This field is required!";
                } else {
                    try {
                        Integer.parseInt(update_interval);
                        if (!update_interval.equals(application_information.update_interval)) {
                            columns.add("update_interval");
                            values.add(update_interval);
                        }
                    } catch (NumberFormatException e) {
                        this.settings.UPDATE_INTERVAL_ERROR_MESSAGE = "Incorrect value format!";
                    }
                }

                if (!columns.isEmpty() && !values.isEmpty()) {

                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", application_information.id);
                    
                    database_connection.update(
                            columns.toArray(new String[columns.size()]),
                            values.toArray(),
                            "application",
                            whereQuery
                    );
                    database_connection.close();
                    
                    //Return true if the application was updated.
                    return true;
                }
                database_connection.close();
            }
            //Return false if some error was thrown.
            return false;
        }
        //Edit application method end.
        
        //Remove application method start.
        /**
         * Removes the application related to the provided id along with all logs related to this application.
         * 
         * @param application_information ApplicationRow object representing the application you're removing.
         * @return true if the application was removed or doesn't exist, false if there was an issue.
         */
        public boolean remove(ApplicationRow application_information) {
            if (this.user_groups.manage_applications && application_information != null &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(application_information.company.id))) {
                
                HashMap whereQuery = new HashMap();
                whereQuery.put("application_id", application_information.id);
                
                database_connection.connect();
                database_connection.delete_row("log", whereQuery);
                
                whereQuery.clear();
                whereQuery.put("id", application_information.id);
                
                database_connection.delete_row("application", whereQuery);
                database_connection.close();
                
                //Return true if the application was successfully deleted.
                return true;
            }
            //Return false if some error was thrown.
            return false;
        }
        //Remove application method end.
        
        //Insert logs method start.
        /**
         * Inserts logs to an existing application.
         * 
         * @param application_information ApplicationRow object representing the application the logs belong to.
         * @param logs A two dimensional array containing the logs being inserted into the database.
         * @return true of the logs were inserted, false if there was an issue.
         */
        public boolean insert_logs(ApplicationRow application_information, String[][] logs) {
            if (user_groups.manage_applications &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(application_information.company.id))) {
                
                database_connection.connect();
                
                String[] columnQuery = { "id" };

                HashMap whereQuery = new HashMap();
                whereQuery.put("id", application_information.id);

                String[][] select = database_connection.select(columnQuery, "application", whereQuery);

                if (select != null && select.length == 1 && select[0].length == columnQuery.length) {
                    
                    columnQuery = new String[] {
                        "application_id",
                        "date",
                        "event"
                    };
                    
                    Object[][] values = new Object[logs.length][3];
                    
                    for (int i = 0; i < logs.length; i++) {
                        values[i] = new Object[] { application_information.id, logs[i][0], logs[i][1] };
                    }
                    
                    database_connection.insert(columnQuery, values, "log");
                    
                    columnQuery = new String[] { "latest_update" };
                    
                    whereQuery.clear();
                    whereQuery.put("id", application_information.id);
                    
                    Object[] latest_update = { new Timestamp(new Date().getTime()) };
                    
                    database_connection.update(columnQuery, latest_update, "application", whereQuery);
                    database_connection.close();
                    return true;
                }
                
                database_connection.close();
            }
            return false;
        }
        //Insert logs method end.
    }
    //Administration.Application object end.
    
    //Administration.Company object start.
    public class Company {
        
        /**
         * The settings related to the Administration.Company object.
         */
        public final Settings settings;
        private final UserGroups user_groups;
        private final CompanyRow user_company;
        
        public Company(UserGroups user_groups, CompanyRow user_company) {
            
            this.settings = new Settings();
            this.user_groups = user_groups;
            this.user_company = user_company;
        }

        //Company settings start.
        public class Settings {
            
            public final String ADMINISTRATOR_COMPANY_ID = "1";
            
            public final int NAME_MAX_LENGTH = 45;
            public final int WEBSITE_MAX_LENGTH = 45;
            public final int DETAILS_MAX_LENGTH = 45;
            
            public String NAME_ERROR_MESSAGE = null;
            public String WEBSITE_ERROR_MESSAGE = null;
            public String DETAILS_ERROR_MESSAGE = null;
            
            /**
             * This method set all error messages back to null.
             */
            public void clear_error_messages() {
                if (this.NAME_ERROR_MESSAGE != null) this.NAME_ERROR_MESSAGE = null;
                if (this.WEBSITE_ERROR_MESSAGE != null) this.WEBSITE_ERROR_MESSAGE = null;
                if (this.DETAILS_ERROR_MESSAGE != null) this.DETAILS_ERROR_MESSAGE = null;
            }
        }
        //Company settings end.
        
        //Create company method start.
        /**
         * Creates a new company in the company table.
         * 
         * @param name The name of the new company.
         * @param website The web site of the new company - can be null.
         * @param details Extra details of the new company - can be null.
         * @return true if a new company was created, false if there was an issue.
         */
        public boolean create(String name, String website, String details) {
            if (this.user_groups.manage_companies && this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP)) {
                
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The company name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                } else {
                    columns.add("name");
                    values.add(name);
                }
                
                if (website != null && website.length() >= 1) {
                    if (website.length() > this.settings.WEBSITE_MAX_LENGTH) {
                        this.settings.WEBSITE_ERROR_MESSAGE = "The website link can't be longer than " + this.settings.WEBSITE_MAX_LENGTH + " characters!";
                    } else {
                        columns.add("website");
                        values.add(website);
                    }
                }

                if (details != null && details.length() >= 1) {
                    if (details.length() > this.settings.DETAILS_MAX_LENGTH) {
                        this.settings.DETAILS_ERROR_MESSAGE = "The company details can't be longer than " + this.settings.DETAILS_MAX_LENGTH + " characters!";
                    } else {
                        columns.add("details");
                        values.add(details);
                    }
                }

                if (!columns.isEmpty() && !values.isEmpty() && columns.contains("name")) {

                    database_connection.connect();
                    database_connection.insert(
                            columns.toArray(new String[columns.size()]),
                            new String[][] { values.toArray(new String[values.size()]) },
                            "company"
                    );
                    database_connection.close();
                    
                    //Return true if company was updated.
                    return true;
                }
            }
            //Return false if some error was thrown.
            return false;
        }
        //Create company method end.

        //Edit company method start.
        /**
         * Update an existing company's information.
         * 
         * @param company_information CompanyRow object containing the information of the company being subject to change.
         * @param name The new name for this company.
         * @param website The new website for this company - can be null.
         * @param details The new details for this company - can be null.
         * @return true if the company information was updated, false if there was an issue.
         */
        public boolean edit(CompanyRow company_information, String name, String website, String details) {
            if (this.user_groups.manage_companies && !this.settings.ADMINISTRATOR_COMPANY_ID.equals(company_information.id) &&
                    (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || this.user_company.id.equals(company_information.id))) {
                
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The company name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                } else if (!name.equals(company_information.name)) {
                    columns.add("name");
                    values.add(name);
                }
                
                if (website != null && website.length() >= 1 &&
                        !website.equals(company_information.website)) {
                    
                    if (website.length() > this.settings.WEBSITE_MAX_LENGTH) {
                        this.settings.WEBSITE_ERROR_MESSAGE = "The website link can't be longer than " + this.settings.WEBSITE_MAX_LENGTH + " characters!";
                    } else {
                        columns.add("website");
                        values.add(website);
                    }
                }

                if (details != null && details.length() >= 1 &&
                        !details.equals(company_information.details)) {
                    
                    if (details.length() > this.settings.DETAILS_MAX_LENGTH) {
                        this.settings.DETAILS_ERROR_MESSAGE = "The company details can't be longer than " + this.settings.DETAILS_MAX_LENGTH + " characters!";
                    } else {
                        columns.add("details");
                        values.add(details);
                    }
                }

                if (!columns.isEmpty() && !values.isEmpty()) {
                    
                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", company_information.id);
                    
                    database_connection.connect();
                    database_connection.update(
                            columns.toArray(new String[columns.size()]),
                            values.toArray(),
                            "company",
                            whereQuery
                    );
                    database_connection.close();
                    
                    //Return true if company was updated.
                    return true;
                }
            }
            //Return false if some error was thrown.
            return false;
        }
        //Edit company method end.
        
        //Remove company method start.
        /**
         * Removes the company related to the provided id along with all of the users and applications related to this company.
         * The main administrator company can't be removed.
         * 
         * @param id Id related to the company being removed.
         * @return true if the company was removed or doesn't exist, false if there was an issue.
         */
        public boolean remove(String id) {
            if (this.user_groups.manage_companies && this.user_groups.manage_users && this.user_groups.manage_applications &&
                    id != null && !this.settings.ADMINISTRATOR_COMPANY_ID.equals(id) &&
                    this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP)) {
                
                String[] columnQuery = { "id" };
                
                HashMap whereQuery = new HashMap();
                whereQuery.put("company_id", id);

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "user", whereQuery);

                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    for (String[] row : select) {
                        user.remove(new CurrentUser(row[0]));
                    }
                }
                
                select = database_connection.select(columnQuery, "application", whereQuery);

                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    for (String[] row : select) {
                        application.remove(new ApplicationRow(row[0]));
                    }
                }
                
                whereQuery.clear();
                whereQuery.put("id", id);
                
                database_connection.delete_row("company", whereQuery);
                database_connection.close();
                
                //Return true if the company and all dependencies was successfully deleted.
                return true;
            }
            //Return false if some error was thrown.
            return false;
        }
        //Remove company method start.
    }
    //Administration.Company object end.
    
    //Administration.UserGroup object start.
    public class UserGroup {
        
        /**
         * The settings related to the Administration.UserGroup object.
         */
        public final Settings settings;
        private final UserGroups user_groups;
        
        public UserGroup(UserGroups user_groups) {
            
            this.settings = new Settings();
            this.user_groups = user_groups;
        }
        
        //UserGroup settings start.
        public class Settings {
            
            public final String ADMINISTRATOR_USER_GROUP = "1";
            
            public final int NAME_MAX_LENGTH = 20;
            
            public String NAME_ERROR_MESSAGE = null;
            
            /**
             * This method set all error messages back to null.
             */
            public void clear_error_messages() {
                if (this.NAME_ERROR_MESSAGE != null) this.NAME_ERROR_MESSAGE = null;
            }
        }
        //UserGroup settings end.
        
        //Create user group method start.
        /**
         * Creates a new user group in the user_groups table.
         * 
         * @param name The name of the new user group.
         * @param view_logs The rights of being able to view logs.
         * @param manage_applications The rights of being able to create, update and delete applications.
         * @param manage_users The rights of being able to create, update and delete users.
         * @param manage_companies The rights of being able to create, update and delete companies.
         * @param manage_groups The rights of being able to create, update and delete user groups.
         * @return true if the new user groups was created, false if there was an issue.
         */
        public boolean create(String name, boolean view_logs, boolean manage_applications, boolean manage_users, boolean manage_companies, boolean manage_groups) {
            if (this.user_groups.manage_groups) {
                boolean proceed = true;
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The user group name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }
                
                if (proceed) {
                    String[] columnQuery = new String[] {
                                "name",
                                "view_logs",
                                "manage_applications",
                                "manage_users",
                                "manage_companies",
                                "manage_groups"
                    };

                    Object[][] values = {
                        {
                                name,
                                view_logs,
                                manage_applications,
                                manage_users,
                                manage_companies,
                                manage_groups
                        }
                    };

                    database_connection.connect();
                    database_connection.insert(columnQuery, values, "user_groups");
                    database_connection.close();
                    
                    //Return true if the user group was successfully created.
                    return true;
                }
            }
            //Return false if some error was thrown.
            return false;
        }
        //Create user group method end.
        
        //Edit user group method start.
        /**
         * Update an existing user group, not being the main administration user group.
         * 
         * @param group_information UserGroups object containing the information related to the user group being subject to change.
         * @param name The new name for this user group.
         * @param view_logs The new view logs right for this user group.
         * @param manage_applications The new manage application right for this user group.
         * @param manage_users The new manage users right for this user group.
         * @param manage_companies The new manage companies right for this user group.
         * @param manage_groups The new manage groups right for this user group.
         * @return true if the user group was updated, false if there was an issue.
         */
        public boolean edit(UserGroups group_information, String name, boolean view_logs, boolean manage_applications, boolean manage_users, boolean manage_companies, boolean manage_groups) {
            if (this.user_groups.manage_groups && !this.settings.ADMINISTRATOR_USER_GROUP.equals(group_information.id)) {
                ArrayList<String> columns = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                
                if (name == null || name.length() < 1) {
                    this.settings.NAME_ERROR_MESSAGE = "This field is required!";
                } else if (name.length() > this.settings.NAME_MAX_LENGTH) {
                    this.settings.NAME_ERROR_MESSAGE = "The user group name can't be longer than " + this.settings.NAME_MAX_LENGTH + " characters!";
                } else if (!name.equals(group_information.name)) {
                    columns.add("name");
                    values.add(name);
                }
                
                if (view_logs != group_information.view_logs) {
                    columns.add("view_logs");
                    values.add(view_logs);
                }
                
                if (manage_applications != group_information.manage_applications) {
                    columns.add("manage_applications");
                    values.add(manage_applications);
                }
                
                if (manage_users != group_information.manage_users) {
                    columns.add("manage_users");
                    values.add(manage_users);
                }
                
                if (manage_companies != group_information.manage_companies) {
                    columns.add("manage_companies");
                    values.add(manage_companies);
                }
                
                if (manage_groups != group_information.manage_groups) {
                    columns.add("manage_groups");
                    values.add(manage_groups);
                }
                
                if (!columns.isEmpty() && !values.isEmpty()) {
                    
                    HashMap whereQuery = new HashMap();
                    whereQuery.put("id", group_information.id);
                    
                    database_connection.connect();
                    database_connection.update(
                            columns.toArray(new String[columns.size()]),
                            values.toArray(),
                            "user_groups",
                            whereQuery
                    );
                    database_connection.close();
                    
                    //Return true if the user group was updated.
                    return true;
                }
            }
            //Return false if some error was thrown.
            return false;
        }
        //Edit user group method end.

        //Remove user group method start.
        /**
         * Removes the user group related to the provided id along with all users related to this user group.
         * The main administration user group can't however be removed.
         * 
         * @param id The id related to the user group being removed.
         * @return true if the user group was removed or doesn't exist, false if there was an issue.
         */
        public boolean remove(String id) {
            if (this.user_groups.manage_groups && this.user_groups.manage_users && id != null && !this.settings.ADMINISTRATOR_USER_GROUP.equals(id)) {
                String[] columnQuery = { "id" };

                HashMap whereQuery = new HashMap();
                whereQuery.put("user_group_id", id);

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "user", whereQuery);

                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    for (String[] row : select) {
                        user.remove(new CurrentUser(row[0]));
                    }
                }
                
                whereQuery.clear();
                whereQuery.put("id", id);
                
                database_connection.connect();
                database_connection.delete_row("user_groups", whereQuery);
                database_connection.close();
                
                //Return true if the user group was successfully deleted.
                return true;
            }
            //Return false if some error was thrown.
            return false;
        }
        //Remove user group method end.
    }
    //Administration.UserGroup object end.
    
    //Administration.Collections object start.
    public class ObjectCollections {
        
        private final UserGroups user_groups;
        private final CompanyRow user_company;
        
        public ObjectCollections(UserGroups user_groups, CompanyRow user_company) {
            
            this.user_groups = user_groups;
            this.user_company = user_company;
        }
        
        //CurrentUser collection begin.
        /**
         * This method retrieves all the users in the database except from the main administrator.
         * 
         * @return An array of CurrentUser objects containing each found user's information.
         */
        public CurrentUser[] users() {
            if (this.user_groups.manage_users) {
                
                String[] columnQuery = {
                        "id",
                        "company_id",
                        "first_name",
                        "last_name",
                        "email",
                        "username",
                        "user_group_id"
                };

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "user");
                database_connection.close();
                
                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    ArrayList<CurrentUser> results = new ArrayList<>();
                    
                    for (String[] row : select) {
                        
                        CurrentUser found_user = new CurrentUser(
                                row[0],
                                row[1],
                                row[2],
                                row[3],
                                row[4],
                                row[5],
                                row[6]
                        );
                        
                        if (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) ||
                                (!found_user.id.equals(user.settings.ADMINISTRATOR_ID) && this.user_company.id.equals(found_user.company.id))) {
                            
                            results.add(found_user);
                        }
                    }
                    return results.toArray(new CurrentUser[results.size()]);
                }
            }
            return null;
        }
        //CurrentUser collection end.
        
        //CompanyRow collection begin.
        /**
         * This method retrieves all the companies in the database except from the main administrator's company.
         * 
         * @return An array of CompanyRow objects containing each found company's information.
         */
        public CompanyRow[] companies() {
            if (this.user_groups.manage_companies ||
                this.user_groups.manage_applications ||
                this.user_groups.manage_users) {
                
                String[] columnQuery = {
                        "id",
                        "name",
                        "website",
                        "details"
                };

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "company");
                database_connection.close();
                
                
                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    ArrayList<CompanyRow> results = new ArrayList<>();
                    
                    for (String[] row : select) {
                        
                        CompanyRow found_company = new CompanyRow(
                                row[0],
                                row[1],
                                row[2],
                                row[3]
                        );
                        
                        if (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) || 
                                (!found_company.id.equals(company.settings.ADMINISTRATOR_COMPANY_ID) && this.user_company.id.equals(found_company.id))) {
                            
                            results.add(found_company);
                        }
                    }
                    return results.toArray(new CompanyRow[results.size()]);
                }
            }
            return null;
        }
        //CompanyRow collection end.
        
        //UserGroups collection begin.
        /**
         * This method retrieves all the user groups in the database.
         * 
         * @return An array of UserGroups objects containing each found user group's information.
         */
        public UserGroups[] user_groups() {
            if (this.user_groups.manage_groups || this.user_groups.manage_users) {
                
                String[] columnQuery = {
                        "id",
                        "name",
                        "view_logs",
                        "manage_applications",
                        "manage_users",
                        "manage_companies",
                        "manage_groups"
                };

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "user_groups");
                database_connection.close();

                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    ArrayList<UserGroups> results = new ArrayList<>();
                    
                    for (String[] row : select) {
                        
                        UserGroups found_user_group = new UserGroups(
                                row[0],
                                row[1],
                                row[2],
                                row[3],
                                row[4],
                                row[5],
                                row[6]
                        );
                        
                        if (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) ||
                                !found_user_group.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP)) {
                            
                            results.add(found_user_group);
                        }
                    }
                    return results.toArray(new UserGroups[results.size()]);
                }
            }
            return null;
        }
        //UserGroups collection end.
        
        //ApplicationRow collection begin.
        /**
         * This method retrieves all the applications in the database.
         * 
         * @return An array of ApplicationRow objects containing each found application's information.
         */
        public ApplicationRow[] applications() {
            if (this.user_groups.manage_applications) {
                
                String[] columnQuery = {
                        "id",
                        "company_id",
                        "name",
                        "latest_update",
                        "update_interval",
                        "api_key",
                        "log_type"
                };

                database_connection.connect();
                String[][] select = database_connection.select(columnQuery, "application");
                database_connection.close();

                if (select != null && select.length >= 1 && select[0].length == columnQuery.length) {
                    ArrayList<ApplicationRow> results = new ArrayList<>();
                    
                    for (String[] row : select) {
                        ApplicationRow found_application = new ApplicationRow(
                                row[0],
                                row[1],
                                row[2],
                                row[3],
                                row[4],
                                row[5],
                                row[6],
                                false
                        );
                        
                        if (this.user_groups.id.equals(user_group.settings.ADMINISTRATOR_USER_GROUP) ||
                                this.user_company.id.equals(found_application.company.id)) {
                            
                            results.add(found_application);
                        }
                    }
                    return results.toArray(new ApplicationRow[results.size()]);
                }
            }
            return null;
        }
        //ApplicationRow collection end.
    }
    //Administration.Collections object end.
}