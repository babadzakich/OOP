package ru.nsu.chuvashov.antlrconfig;

import com.example.parser.configurationBaseVisitor;
import com.example.parser.configurationParser;
import java.time.LocalDate;
import java.util.*;
import lombok.Getter;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.nsu.chuvashov.antlrconfig.configholder.*;

/**
 * Visitor class to process my config using antlr4.
 */
@Getter
public class MyVisitor extends configurationBaseVisitor<Object> {
    private Tools tools;
    private final List<Group> groups = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final Map<String, List<String>> studentCheckers = new HashMap<>();
    private final List<ControlPoint> controlPoints = new ArrayList<>();
    private final Set<String> imports = new HashSet<>();

    /**
     * Start of the tree parse.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public Object visitProgram(configurationParser.ProgramContext ctx) {
        if (ctx.imports() != null && !ctx.imports().isEmpty()) {
            visitImports(ctx.imports());
            return null;
        }
        
        
        if (ctx.groupEntry() != null) {
            for (configurationParser.GroupEntryContext
                    groupEntryCtx : ctx.groupEntry()) {
                visitGroupEntry(groupEntryCtx);
            }
        }
        
        if (ctx.tasksEntry() != null) {
            for (configurationParser.TasksEntryContext
                    tasksEntryCtx : ctx.tasksEntry()) {
                visitTasksEntry(tasksEntryCtx);
            }
        }
        
        if (ctx.toolsEntry() != null && !ctx.toolsEntry().isEmpty()) {
            this.tools = visitToolsEntry(ctx.toolsEntry(0));
        }
        
        if (ctx.checkerEntry() != null) {
            for (configurationParser.CheckerEntryContext
                    checkerEntryCtx : ctx.checkerEntry()) {
                visitCheckerEntry(checkerEntryCtx);
            }
        }
        
        if (ctx.controlpointsEntry() != null) {
            for (configurationParser.ControlpointsEntryContext
                    controlpointsEntryCtx : ctx.controlpointsEntry()) {
                visitControlpointsEntry(controlpointsEntryCtx);
            }
        }
        return null;
    }

    /**
     * The branch for imports file.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public List<String> visitImports(configurationParser.ImportsContext ctx) {
        for (TerminalNode stringNode : ctx.STRING()) {
            String importStr = stringNode.getText();
            imports.add(importStr.substring(1, importStr.length() - 1));
        }
        return null;
    }

    /**
     * Branch for group.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public Group visitGroupEntry(configurationParser.GroupEntryContext ctx) {
        for (configurationParser.GroupContext group : ctx.group()) {
            visitGroup(group);
        }
        return null;
    }

    /**
     * Parse the whole group with every student.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public Group visitGroup(configurationParser.GroupContext ctx) {
        int id = Integer.parseInt(ctx.INT().getText());
        Group group = new Group(id);
        for (configurationParser.StudentContext studentCtx : ctx.student()) {
            Student student = visitStudent(studentCtx);
            group.getStudents().add(student);
        }
        groups.add(group);
        return null;
    }

    /**
     * Parse student branch.
     *
     * @param ctx the parse tree.
     * @return student data.
     */
    @Override
    public Student visitStudent(configurationParser.StudentContext ctx) {
        String id = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.StudentBodyContext bodyCtx = ctx.studentBody();
        String name = bodyCtx.STRING(0).getText()
                .substring(1, bodyCtx.STRING(0).getText().length() - 1);
        String github = bodyCtx.STRING(1).getText()
                .substring(1, bodyCtx.STRING(1).getText().length() - 1);
        return new Student(github, name, id);
    }

    /**
     * Parse all tasks.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public List<Task> visitTasksEntry(configurationParser.TasksEntryContext ctx) {
        for (configurationParser.TaskContext taskCtx : ctx.task()) {
            Task task = visitTask(taskCtx);
            tasks.add(task);
        }
        return null;
    }

    /**
     * Parse single task.
     *
     * @param ctx the parse tree.
     * @return one task data.
     */
    @Override
    public Task visitTask(configurationParser.TaskContext ctx) {
        String id = ctx.STRING().getText()
                .substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.TaskBodyContext bodyCtx = ctx.taskBody();
        String name = bodyCtx.STRING().getText()
                .substring(1, bodyCtx.STRING().getText().length() - 1);
        LocalDate firstCommit = visitDate(bodyCtx.date(0));
        LocalDate lastCommit = visitDate(bodyCtx.date(1));
        int points = Integer.parseInt(bodyCtx.INT().getText());
        return new Task(id, name, firstCommit, lastCommit, points);
    }

    /**
     * Tool entry.
     *
     * @param ctx the parse tree.
     * @return Tools data.
     */
    @Override
    public Tools visitToolsEntry(configurationParser.ToolsEntryContext ctx) {
        configurationParser.ToolsBodyContext bodyCtx = ctx.toolsBody();
        String criteria = bodyCtx.STRING(0).getText()
                .substring(1, bodyCtx.STRING(0).getText().length() - 1);
        String build = bodyCtx.STRING(1).getText()
                .substring(1, bodyCtx.STRING(1).getText().length() - 1);
        return new Tools(criteria, build);
    }

    /**
     * Parse additional info data.
     *
     * @param ctx the parse tree.
     * @return nothing
     */
    @Override
    public List<StudentChecker> visitCheckerEntry(configurationParser.CheckerEntryContext ctx) {
        for (configurationParser.StudentCheckContext
                studentCheckCtx : ctx.studentCheck()) {
            visitStudentCheck(studentCheckCtx);
        }
        return null;
    }

    /**
     * Parse every student`s additional info.
     *
     * @param ctx the parse tree.
     * @return nothing
     */
    @Override
    public StudentChecker visitStudentCheck(configurationParser.StudentCheckContext ctx) {
        String id = ctx.STRING(0).getText()
                .substring(1, ctx.STRING(0).getText().length() - 1);
        List<String> checks = new ArrayList<>();
        for (int i = 1; i < ctx.STRING().size(); i++) {
            String check = ctx.STRING(i).getText()
                    .substring(1, ctx.STRING(i).getText().length() - 1);
            checks.add(check);
        }
        studentCheckers.put(id, checks);
        return null;
    }

    /**
     * Parse control points.
     *
     * @param ctx the parse tree.
     * @return nothing.
     */
    @Override
    public List<ControlPoint> visitControlpointsEntry(
            configurationParser.ControlpointsEntryContext ctx) {
        for (configurationParser.ControlpointContext
                cpCtx : ctx.controlpoint()) {
            ControlPoint cp = visitControlpoint(cpCtx);
            controlPoints.add(cp);
        }
        return null;
    }

    /**
     * Parse single control point.
     *
     * @param ctx the parse tree.
     * @return control point data.
     */
    @Override
    public ControlPoint visitControlpoint(configurationParser.ControlpointContext ctx) {
        String id = ctx.STRING().getText()
                .substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.ControlpointBodyContext bodyCtx = ctx.controlpointBody();
        LocalDate date = visitDate(bodyCtx.date());
        int mark3 = Integer.parseInt(bodyCtx.INT(0).getText());
        int mark4 = Integer.parseInt(bodyCtx.INT(1).getText());
        int mark5 = Integer.parseInt(bodyCtx.INT(2).getText());
        return new ControlPoint(id, date, new int[]{mark3, mark4, mark5});
    }

    /**
     * Parse date.
     *
     * @param ctx the parse tree.
     * @return Date.
     */
    @Override
    public LocalDate visitDate(configurationParser.DateContext ctx) {
        int day = Integer.parseInt(ctx.INT(0).getText());
        int month = Integer.parseInt(ctx.INT(1).getText());
        int year = Integer.parseInt(ctx.INT(2).getText());
        return LocalDate.of(year, month, day);
    }
}
