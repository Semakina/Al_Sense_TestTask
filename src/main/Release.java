public record Release(int startDay, int endDay) {

    public Release(String startDay, String daysToValidate) {
        this(
                Integer.parseInt(startDay),
                Integer.parseInt(startDay) + Integer.parseInt(daysToValidate) - 1
        );
    }

    public boolean hasIntersection(Release release) {
        return !(this.endDay < release.startDay || release.endDay < this.startDay);
    }

    public boolean isFitInSprint(int sprintDays) {
        return this.endDay <= sprintDays;
    }
}

