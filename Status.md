# Project Status
## Authenticate
### Log In - Complete
- User is prompted to enter username and password.
- Higher level users have options to log in with basic access.
- Log in attempts are logged by the system.
### Log Out - Complete
- User is logged out of the system.
- User has no access rights.
## Documents
### Authorisation Check - Complete
- App Controller stop users from accessing commands they would never be allowed access to.
- HR Database also checks access level for the user performing the command and makes logs of all attempts, noting success or failure.
## Personal Details
### Read Personal Details - Complete
- Personal details documents can be read by the employee the document pertains to or members of HR. 
### Create Personal Details - Complete
- HR employees can create a personal details record that can then be filled with the correct information. 
- No other employee types can create these documents. 
### Amend Personal Details - Complete
- Created personal details records can be amended by HR employees and the employee the document pertains to. 
## Annual Reviews
### Allocate Reviewer - Complete
- HR employees can allocate a secondary reviewer to an employee. The first reviewer is always the employees direct superior, and is assigned automatically. 
### Create New Review Record - Complete
- The reviewee can create a new review document for editing by all participants.
- New document is stored in the database, identified by the reviewee's employee number and the year of the review. 
### Read Review Record - Complete
- The reviewee and the reviewers can all read all review documents for that employee. 
### Amend Review Record - Complete
- The review document can be amended by all review participants. 
### Perform Review - Complete
- The participants can use the above functionality to fill in the document. 
- Once the participants have all signed the review, the document becomes read-only and can no longer be changed. 
- Reviewers have a review mode in the system that will allow them to participate in the review. They are not able to review employees that they have not been assigned to. 
    - Reviewers in this mode are unable to perform their usual commands, and must re login to regain full privileges. 