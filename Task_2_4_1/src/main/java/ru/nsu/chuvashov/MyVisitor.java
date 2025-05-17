package ru.nsu.chuvashov;

import lombok.Getter;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.nsu.chuvashov.configholder.*;

import com.example.parser.configurationBaseVisitor;
import com.example.parser.configurationParser;


import java.util.*;

@Getter
public class MyVisitor extends configurationBaseVisitor<Object> {
    private Tools tools;
    final private List<Group> groups = new ArrayList<>();
    final private List<Task> tasks = new ArrayList<>();
    final private Map<String, List<String>> studentCheckers = new HashMap<>();
    final private List<ControlPoint> controlPoints = new ArrayList<>();
    private final Set<String> imports = new HashSet<>();


    @Override
    public Object visitProgram(configurationParser.ProgramContext ctx) {
        if (ctx.imports() != null && !ctx.imports().isEmpty()) {
            visitImports(ctx.imports());
            return null;
        }
        
        
        if (ctx.groupEntry() != null) {
            for (configurationParser.GroupEntryContext groupEntryCtx : ctx.groupEntry()) {
                visitGroupEntry(groupEntryCtx);
            }
        }
        
        if (ctx.tasksEntry() != null) {
            for (configurationParser.TasksEntryContext tasksEntryCtx : ctx.tasksEntry()) {
                visitTasksEntry(tasksEntryCtx);
            }
        }
        
        if (ctx.toolsEntry() != null && !ctx.toolsEntry().isEmpty()) {
            this.tools = visitToolsEntry(ctx.toolsEntry(0));
        }
        
        if (ctx.checkerEntry() != null) {
            for (configurationParser.CheckerEntryContext checkerEntryCtx : ctx.checkerEntry()) {
                visitCheckerEntry(checkerEntryCtx);
            }
        }
        
        if (ctx.controlpointsEntry() != null) {
            for (configurationParser.ControlpointsEntryContext controlpointsEntryCtx : ctx.controlpointsEntry()) {
                visitControlpointsEntry(controlpointsEntryCtx);
            }
        }
        
        return null;
    }

    @Override
    public List<String> visitImports(configurationParser.ImportsContext ctx) {
        for (TerminalNode stringNode : ctx.STRING()) {
            String importStr = stringNode.getText();
            imports.add(importStr.substring(1, importStr.length() - 1));
        }
        return null;
    }

    @Override
    public Group visitGroupEntry(configurationParser.GroupEntryContext ctx) {
        return visitGroup(ctx.group());
    }

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

    @Override
    public Student visitStudent(configurationParser.StudentContext ctx) {
        String id = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.StudentBodyContext bodyCtx = ctx.studentBody();
        String name = bodyCtx.STRING(0).getText().substring(1, bodyCtx.STRING(0).getText().length() - 1);
        String github = bodyCtx.STRING(1).getText().substring(1, bodyCtx.STRING(1).getText().length() - 1);
        return new Student(github, name, id);
    }

    @Override
    public List<Task> visitTasksEntry(configurationParser.TasksEntryContext ctx) {
        for (configurationParser.TaskContext taskCtx : ctx.task()) {
            Task task = visitTask(taskCtx);
            tasks.add(task);
        }
        return null;
    }

    @Override
    public Task visitTask(configurationParser.TaskContext ctx) {
        String id = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.TaskBodyContext bodyCtx = ctx.taskBody();
        String name = bodyCtx.STRING().getText().substring(1, bodyCtx.STRING().getText().length() - 1);
        Date firstCommit = visitDate(bodyCtx.date(0));
        Date lastCommit = visitDate(bodyCtx.date(1));
        int points = Integer.parseInt(bodyCtx.INT().getText());
        return new Task(id, name, firstCommit, lastCommit, points);
    }

    @Override
    public Tools visitToolsEntry(configurationParser.ToolsEntryContext ctx) {
        configurationParser.ToolsBodyContext bodyCtx = ctx.toolsBody();
        String criteria = bodyCtx.STRING(0).getText().substring(1, bodyCtx.STRING(0).getText().length() - 1);
        String build = bodyCtx.STRING(1).getText().substring(1, bodyCtx.STRING(1).getText().length() - 1);
        return new Tools(criteria, build);
    }

    @Override
    public List<StudentChecker> visitCheckerEntry(configurationParser.CheckerEntryContext ctx) {
        for (configurationParser.StudentCheckContext studentCheckCtx : ctx.studentCheck()) {
            visitStudentCheck(studentCheckCtx);
        }
        return null;
    }

    @Override
    public StudentChecker visitStudentCheck(configurationParser.StudentCheckContext ctx) {
        String id = ctx.STRING(0).getText().substring(1, ctx.STRING(0).getText().length() - 1);
        List<String> checks = new ArrayList<>();
        for (int i = 1; i < ctx.STRING().size(); i++) {
            String check = ctx.STRING(i).getText().substring(1, ctx.STRING(i).getText().length() - 1);
            checks.add(check);
        }
        studentCheckers.put(id, checks);
        return null;
    }

    @Override
    public List<ControlPoint> visitControlpointsEntry(configurationParser.ControlpointsEntryContext ctx) {
        for (configurationParser.ControlpointContext cpCtx : ctx.controlpoint()) {
            ControlPoint cp = visitControlpoint(cpCtx);
            controlPoints.add(cp);
        }
        return null;
    }

    @Override
    public ControlPoint visitControlpoint(configurationParser.ControlpointContext ctx) {
        String id = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
        configurationParser.ControlpointBodyContext bodyCtx = ctx.controlpointBody();
        Date date = visitDate(bodyCtx.date());
        int mark3 = Integer.parseInt(bodyCtx.INT(0).getText());
        int mark4 = Integer.parseInt(bodyCtx.INT(1).getText());
        int mark5 = Integer.parseInt(bodyCtx.INT(2).getText());
        return new ControlPoint(id, date, new int[]{mark3, mark4, mark5});
    }

    @Override
    public Date visitDate(configurationParser.DateContext ctx) {
        int day = Integer.parseInt(ctx.INT(0).getText());
        int month = Integer.parseInt(ctx.INT(1).getText());
        int year = Integer.parseInt(ctx.INT(2).getText());
        return new Date(day, month, year);
    }
}
