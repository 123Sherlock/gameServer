package cgx.core.command;

public interface Command {

    void execute(CmdCtx cmdCtx) throws Exception;
}
