package my.vaadin.logaggretatorserver;

import java.util.HashMap;

public class Administration {

    public User user;
    public Application application;
    public Company company;
    public UserGroup user_group;

    private final ServerDataBase database_connection = new ServerDataBase();
    
    public Administration(UserGroups user_groups) {
        
        this.user = new User(user_groups);
        this.application = new Application(user_groups);
        this.company = new Company(user_groups);
        this.user_group = new UserGroup(user_groups);
    }
    
    //Administration.User object start.
    public class User {
        
        public Settings settings;
        private final UserGroups user_groups;
        
        public User(UserGroups user_groups) {
            
            this.settings = new Settings();
            this.user_groups = user_groups;
        }
        
        //User settings start.
        public class Settings {
            
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
        }
        //User settings end.
        
        //Create user method start.
        public boolean create(String first_name, String last_name, String email, String username, String password, String company_id, String user_group_id) {
            if (this.user_groups.manage_users) {
                boolean proceed = true;
                database_connection.connect();
                
                if (first_name == null) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (first_name.length() > this.settings.FIRST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your first name can't be longer than " + this.settings.FIRST_NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (last_name == null) {
                    this.settings.LAST_NAME_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (last_name.length() > this.settings.LAST_NAME_MAX_LENGTH) {
                    this.settings.FIRST_NAME_ERROR_MESSAGE = "Your last name can't be longer than " + this.settings.LAST_NAME_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (email == null) {
                    this.settings.EMAIL_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (email.length() > this.settings.EMAIL_MAX_LENGTH) {
                    this.settings.EMAIL_ERROR_MESSAGE = "Your email can't be longer than " + this.settings.EMAIL_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (username == null) {
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

                if (password == null) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "This field is required!";
                    if (proceed) proceed = false;
                } else if (password.length() > this.settings.PASSWORD_MAX_LENGTH) {
                    this.settings.PASSWORD_ERROR_MESSAGE = "Your password can't be longer than " + this.settings.PASSWORD_MAX_LENGTH + " characters!";
                    if (proceed) proceed = false;
                }

                if (company_id == null) {
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

                if (user_group_id == null) {
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
                                password,
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
    }
    //Administration.User object end.
    
    //Administration.Application object start.
    public class Application {
        
        private final UserGroups user_groups;
        
        public Application(UserGroups user_groups) {
            
            this.user_groups = user_groups;
        }
        
    }
    //Administration.Application object end.
    
    //Administration.Company object start.
    public class Company {
        
        private final UserGroups user_groups;
        
        public Company(UserGroups user_groups) {
            
            this.user_groups = user_groups;
        }
    }
    //Administration.Company object end.
    
    //Administration.UserGroup object start.
    public class UserGroup {
        
        private final UserGroups user_groups;
        
        public UserGroup(UserGroups user_groups) {
            
            this.user_groups = user_groups;
        }
    }
    //Administration.UserGroup object end.
}