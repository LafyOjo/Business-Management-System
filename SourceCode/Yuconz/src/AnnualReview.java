/**
 * subclass of document
 * stores the details needed for employee's annual review record
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class AnnualReview extends Document{
    private String name;
    private String firstReviewer;
    private String secondReviewer;
    private String section;
    private String jobTitle;
    private String[] objectives;
    private String[] achievements;
    private String summary;
    private String[] goals;
    private String[] reviewerComments;
    private boolean signedByReviewee;
    private boolean signedByReviewerOne;
    private boolean isSignedByReviewerTwo;
    private String year;
    private boolean readOnly;

    /**
     * constructor
     * @param empNo employee number of employee who created the document
     */
    public AnnualReview(String empNo) {
        super(empNo);
        this.name = "";
        this.firstReviewer = "";
        this.secondReviewer = "";
        this.section = "";
        this.jobTitle = "";
        this.objectives = null;
        this.achievements = null;
        this.summary = "";
        this.goals = null;
        this.reviewerComments = null;
        this.signedByReviewee = false;
        this.signedByReviewerOne = false;
        this.isSignedByReviewerTwo = false;
        this.year = "";
        this.readOnly = false;
    }

    /**
     * returns the name of the reviewee
     * @return name of reviewee
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the reviewee
     * @param name - name of reviewee
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the name of the first reviewer
     * @return name of the reviewer
     */
    public String getFirstReviewer() {
        return firstReviewer;
    }

    /**
     * sets the name of the first reviewer
     * @param firstReviewer - name of reviewer
     */
    public void setFirstReviewer(String firstReviewer) {
        this.firstReviewer = firstReviewer;
    }

    /**
     * returns the name of the second reviewer
     * @return name of second reviewer
     */
    public String getSecondReviewer() {
        return secondReviewer;
    }

    /**
     * sets the name of the second reviewer
     * @param secondReviewer - name of second reviewer
     */
    public void setSecondReviewer(String secondReviewer) {
        this.secondReviewer = secondReviewer;
    }

    /**
     * returns the section they work in
     * @return section they work in
     */
    public String getSection() {
        return section;
    }

    /**
     * sets the section they work in
     * @param section - their job sector
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     * returns a job title
     * @return a job title
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * set their job title
     * @param jobTitle - their job title
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * returns their objectives
     * @return their objectives
     */
    public String[] getObjectives() {
        return objectives;
    }

    /**
     * sets their objectives
     * @param objectives - their objectives
     */
    public void setObjectives(String[] objectives) {
        this.objectives = objectives;
    }

    /**
     * returns their achievements
     * @return their achievements
     */
    public String[] getAchievements() {
        return achievements;
    }

    /**
     * sets their achievements
     * @param achievements - their achievements
     */
    public void setAchievements(String[] achievements) {
        this.achievements = achievements;
    }

    /**
     * returns a summary
     * @return a summery
     */
    public String getSummary() {
        return summary;
    }

    /**
     * sets a summery
     * @param summary - a summery
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * returns a set of goals
     * @return set of goals
     */
    public String[] getGoals() {
        return goals;
    }

    /**
     * sets goals
     * @param goals - their goals
     */
    public void setGoals(String[] goals) {
        this.goals = goals;
    }

    /**
     * returns reviewer comments
     * @return reviewer comments
     */
    public String[] getReviewerComments() {
        return reviewerComments;
    }

    /**
     * sets reviewer comments
     * @param reviewerComments - reviewer comments
     */
    public void setReviewerComments(String[] reviewerComments) {
        this.reviewerComments = reviewerComments;
    }

    /**
     * returns true if the reviewee has signed and false otherwise
     * @return true or false
     */
    public boolean isSignedByReviewee() {
        return signedByReviewee;
    }

    /**
     * sets signedByReviewee to true when the reviewee signs
     * @param signedByReviewee - reviewee signature
     */
    public void setSignedByReviewee(boolean signedByReviewee) {
        this.signedByReviewee = signedByReviewee;
    }

    /**
     * returns true if reviewer one has signed and false otherwise
     * @return true or false
     */
    public boolean isSignedByReviewerOne() {
        return signedByReviewerOne;
    }

    /**
     * sets the signature for reviewer one to true when signed
     * @param signedByReviewerOne - reviewer one's signature
     */
    public void setSignedByReviewerOne(boolean signedByReviewerOne) {
        this.signedByReviewerOne = signedByReviewerOne;
    }

    /**
     * returns true if the second reviewer has signed
     * @return true or false
     */
    public boolean isSignedByReviewerTwo() {
        return isSignedByReviewerTwo;
    }

    /**
     * sets the signature for the second reviewer to true when signed
     * @param signedByReviewerTwo - second reviewers signature
     */
    public void setSignedByReviewerTwo(boolean signedByReviewerTwo) {
        isSignedByReviewerTwo = signedByReviewerTwo;
    }

    /**
     * returns the year of the annual review
     * @return year of annual review
     */
    public String getYear() {
        return year;
    }

    /**
     * sets the year of the annual review
     * @param year - year of annual review
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * returns whether this document is read only
     * @return boolean of whether this is read only
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * set the read only status of this document
     * @param readOnly value for whether this document is read only or not
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
