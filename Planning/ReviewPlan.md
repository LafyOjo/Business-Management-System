# Review Plan 
Progress tracking is performed by the meeting documents combined with the gantt chart. 

* The meeting documents contain a list of all tasks that are yet to be begun, in progress, and have been completed. 
    * The tasks in progress mark who those tasks have been assigned to and when they are due to be delivered to the rest of the team for review. 
    * The completed tasks are accompanied in the meeting documents with a date of completion. 
    * This allows the tasks to cascade down the meeting notes as time passes, so it is easy to review progress in the project by how many tasks are left to complete versus how have have been finished. 
* The gantt chart is our second progress review tool, it is to be updated each Friday at the start of the meeting. 

A progress review will take place during the meeting on a Friday to ensure that the team is on track to meet the deadlines set out in the previous meetings. If there are elements that are not on track to be completed by their due date, the task may need to be reassigned to another team member with more time or have more people assigned to it to ensure completion. 
## Document Review
#### Review Points
* Documents are expected to follow the provided templates for the project, so that they have a consistent style. 

* The meeting plan, for the date a document was assigned as a task, will contain the expected content of the document in the "Discussed Topics" section. When it is reviewed the document **MUST** cover at least those areas. If it does not, it will not be marked as completed until it meets this condition. 

#### Review Methods
* Documents are to be reviewed during the meeting in which they are due, and are to be reviewed by the **at least one** other, non-PM member of the team before being accepted into the project. 

* Documents will also be reviewed before submission to Yuconz by the project manager before they are sent. This will be done at least 3 days before the submission deadline to ensure that any issues can be fixed before the deadline, and will be performed via the issue system on GitLab. 

Performing the review process this way passes all documents through two different filters, to ensure a higher level of quality than would be possible with a single review when the document is added to the project. 

## Software Review
#### Review Points
* The software documentation **MUST** comply with the points laid out in the [Quality Assurance Plan](qualityAssurancePlan.md).
* The user **MUST** note in the merge request for their submission what tests the software now passes, to ensure the project is not failing tests it passed before. More detail can be found in the [Testing Plan](TestingPlan.md).

#### Review Methods
* Pair programming
    * This method will allow for created software to be seen by two people during its development and so increases the likelihood that small bugs are avoided early on.
    * By performing the review as the software is being developed, the person reviewing is not presented with a large wall of code to read, and so is much less likely to miss small errors or breach of the above points. 
    
* Merge reviews
    * Software that is being added to the main branch of the project (meaning that it is considered complete) must be reviewed by the PM before being merged.
    * The PM is expected to check over the software and ensure that it meets the above points. If it does not, the GitLab issues system should be used to alert the programmer of that element to the issue so that it can be fixed. 