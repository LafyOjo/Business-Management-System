# Changelog

Notable changes to the source code for this project will be recorded here. 

## Version - 2019/03/23
### Added
* Functionality for Annual Reviews.
* Test classes for annual reviews. 

## Changed
* SQLite databases are now a single database. 
* Access level now has extra reviewer level. 
* Passwords are now stored as hashed and salted data blobs. 

## Version - 2019/03/04

### Added
* Enumerated list for access level.
* Updated tests for new database functionality. 

### Changed
* AuthServer and HRDatabase now interact with static SQLite databases.
* AuthServer and HRDatabase now write login and authorisation attempt logs in .csv files. 
* Methods have moved from User classes to AppController and HRDatabase. 

## Version - 2019/02/16

### Added
* Document class and subclasses - PersonalDetails, AnnualReview
* Address
* AuthRecord
* HRDatabase
* AuthServer - printLoginRecords method - method to print all login records instead of returning them.
* AuthServer - changePrivileges method - method to to lower user privileges to employee level from AuthServer
### Changed
* EVERY METHOD THAT REQUIRES CREDENTIALS ONLY CURRENTLY WORK WITH "HREmployee"
* AppController - checkSuccess method - returns success or fail if a credential specific method is called
* AppController - now takes a HRDatabase as well as the usual AuthServer
### Fixed
* AppController - requestPrivileges method - calls a method to change the current users privileges



## Version - 2019/02/14

### Changed
* AuthServer - addDetails method - added "type" parameter.
### Added
* User class
* Subclasses of User - HREmployee, Employee, Director, Manager



## Version - 2019/02/12v2

### Changed
* Main - main method - minor changes to authServer login details
### Fixed
* AppController - logout method - logs out no matter what



## Version - 2019/02/12v1

### Added
* Main class - creates all objects needed to start the program
### Changed
* AppController - runController method - now has ability to logout, add/remove login details from AuthServer.
* LoginRecord - now has methods to get the empNo, date or both of record
### Fixed
* AppController - login method - now takes parameters
### Removed
AppController - main method - moved from the AppController to Main class



## Version - 2019/02/08

### Changed
* runController method - on logout, page is redirected to login.



## Version - 2019/02/05

### Added
* LoginRecord class
* login method to AppController
* logout method to AppController
* AuthServer class