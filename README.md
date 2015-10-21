CS2103T Project README

Format to store in file is:

--- Floating Tasks ---

float;taskName;todo

float;taskName;done

--- Tasks ---

task;taskName;todo;251215

task;taskName;done;251215

--- Events ---

event;eventName;todo;081015;2359;091015;0001

event;eventName;done;071015;1000;071015;1315

==========Views==========

Switching views:
For now the commands are: 
"def" (default),
"all" (all),
"hist" (history),
"unres" (unresolved), 
"search" (search), 
"help" (help), 
"exit" (exit)

20151018 Update: Only "def" and "all" currently work. All view is incorrect, it still uses default view until getAll methods are done.

20151020 Update: "hist" now works correctly as well.

20151022 Update: "help" now works, opens and closes a separate help stub.
