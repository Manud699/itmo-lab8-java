package client.cli;

public class InputAbortedException extends  RuntimeException{
    public InputAbortedException() {
        super("Input was aborted by the user.");
    }
}
