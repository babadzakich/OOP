grammar configuration;

program: (groupEntry | tasksEntry | toolsEntry | checkerEntry | controlpointsEntry)* | imports;

imports: ('import' STRING)+ ;

groupEntry: 'groups' '{' group '}';
group: 'group' INT '{' student* '}';
student:  STRING '{' studentBody '}';
studentBody: 'Name' STRING
             'Github' STRING;

tasksEntry: 'tasks' '{' task* '}' ;
task: STRING '{' taskBody '}' ;
taskBody: 'Name:' STRING
          'First commit:' date
          'Last commit:' date
          'Points:' INT ;

toolsEntry: 'tools' '{' toolsBody '}';
toolsBody: 'Criteria:' STRING
            'Build:' STRING  ;

checkerEntry: 'checker' '{' studentCheck* '}' ;
studentCheck: STRING ':' '{' (STRING | STRING ',' )* '}' ;

controlpointsEntry: 'controlpoints' '{' controlpoint* '}';
controlpoint: STRING '{' controlpointBody '}' ;
controlpointBody: 'Date' date
                  'Marks' '{'
                  '3:' INT
                  '4:' INT
                  '5:' INT '}' ;

date: INT '.' INT '.' INT;

STRING : '"' .*? '"';
INT : [0-9]+ ;
WS      : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;