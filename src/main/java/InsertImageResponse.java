import java.util.List;

public class InsertImageResponse {

    private List<Image> result;
    private boolean success;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public InsertImageResponse(List<Image> result, boolean success, String status) {
        this.result = result;
        this.success = success;
        this.status = status;
    }

    public List<Image> getResult() {
        return result;
    }

    public void setResult(List<Image> result) {
        this.result = result;
    }
}
