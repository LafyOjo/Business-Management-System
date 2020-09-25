# Testing and Issue Control Plan
## Testing

Tests will be done before each push to the main server repository, and must follow some pass rules before pushing:
* Any tests that pass when the branch was made **MUST NOT** fail when the branch is merged into the master branch.
* Any bugs that stop previously passed tests from passing **MUST** be fixed before merging. 
* Tests **MUST** pass on another team member's machine before merging with the master branch. 
* After merging, the master branch **MUST** pass all previously passed tests or the merge will be reverted and fixed.
* Tests are to be implemented as JUnit tests, aiming for a high level of code and branch coverage. 
* Any functionality that is found not to be tested should have a test written for it and added here; with a note of when it was added. This note should be included in the amendments section at the end of the document to track changes.
### Planned Tests
* **AppController**
    * Attempt login with valid details (all user types)
        * Passes if all log in sucessfully.
    * Attempt login with invalid empNo (all user types)
        * Passes if all login attempts denied.
    * Attempt login with invalid empNo (all user types)
        * Passes if all login attempts denied.
    * Log out of the system after logging in. 
        * Passes if the user is logged in before the command and logged out after. 
    * Set basic access after logging in. (all user types)
        * Passes if logged in user now has basic system access.
    * Check access level in class matches database. (all user types)
        * Passes if access levels match.
    * Check if the system found the right user in the HR database.
        * Passes if the user from the HR Database has the same empID used to log in.

* **AuthServer**
    * Authenticate with valid details (all user types)
        * Passes if right access level is returned. 
    * Authenticate with wrong empNo and password (and cases) (all user types)
        * Passes if authenticate returns "denied".
    * Insert new login into the database.
        * Passes if new user is authenticated. 
    * Delete a login from the database.
        * Passes if deleted login no longer authenticates. 

* **HRDatabase - Personal Details**
    * Get an instance of all user types from the database. 
        * Passes if a user object with all the correct information is returned.
    * Add a new user to the HRDatabase.
        * Passes if a new user can be added only once. 
    * Read a newly added personal details document from the database as HR employee.
        * Passes if the new document has the right staff ID. 
    * Read an employee's own personal details document.
        * Passes if correct document is returned. 
    * Read document for non-existing employee
        * Passes if null returned. 
    * Create a new personal details document as other employee.
        * Passes if create document fails. 
    * Create a new personal details document as HR employee. 
        * Passes if the new document can only be added once. 
    * Amend a personal details document as HR employee.
        * Passes if new values are stored in the document. 
    * Amend own personal details document.
        * Passes if document amended.
    * Set manager to another user.
        * Passes if database contains new manager.  

* **HRDatabase - Annual Reviews**
    * Allocate a second reviewer to an employee.
        * Passes if second reviewer stored in database. 
    * Create document as any employee.
        * Passes if document cannot be created without second reviewer assigned, and if attempted to be created by a different employee. 
    * Read document pertaining to logged in employee.
        * Passes if document is successfully delivered.
    * Read document as a manager of an employee.
        * Passes if nothing is delivered when not in reviewer mode and is delivers when in reviewer mode. 
    * Amend document that is not completed as reviewer.
        * Passes if edited document is saved to the database. 
    * Amend incomplete document as reviewed employee.
        * Passes if edited document is saved to database.
    * Sign off a review and attempt to edit it.
        * Passes if review can no longer be edited. 

## Bug Reporting
Bug reports are to be handled using the 'Issue' system on GitLab. 

The person who is making the report should assign it to the person responsible for that part of the program. 
This information can be found by referring to the latest meeting documents, which will contain the person who is assigned to that part of the project. 

## Amendments
* 2019-02-11:  Document amended to include the type of tests being used (JUnit) and mentions the amendments section. 
* 2019-03-03: Document will now be split by class once the test classes are written. Unwritten classes will remain by use case until covered elsewhere. 
* 2019-03-04: Test cases added for HRDatabase. 


* 2019-03-23: Added Test for changing manager.
* 2019-03-23: Added tests for annual reviews in HRDatabase. 