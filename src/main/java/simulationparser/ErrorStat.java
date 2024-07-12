package simulationparser;

import java.util.Objects;

public class ErrorStat {

    private String errorName;
    private Integer count;

    public ErrorStat(String errorName, Integer count) {
        this.errorName = errorName;
        this.count = count;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorStat errorStat = (ErrorStat) o;
        return Objects.equals(errorName, errorStat.errorName);
    }
}
