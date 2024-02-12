from treelib import Node, Tree
from tree_sitter import Language, Parser
import math
import re
import math

Language.build_library(
    # Store the library in the `build` directory
    "build/my-languages.so",
    # Include one or more languages
    ["./tree-sitter-java"],
)

JAVA = Language("build/my-languages.so", "java")
parser = Parser()
parser.set_language(JAVA)

with open("Operation2.java", "r") as f:

    source_code = f.read()

src = bytes(
    source_code,
    "utf-8",
)


def read_callable(byte_offset, point):
    return src[byte_offset : byte_offset + 1]

tree = parser.parse(read_callable)

curr_node = tree.root_node
cst_tree = Tree()
arethmatc_operators={'+','-','*','/','%'}
assignment_operators={'=','+=', '-=', '*=', '/=', '%='}
relational_operators={ '==', '!=', '<', '>', '<=', '>='}
logical_operators={'&&', '||', '!',"instanceof"} 
ternary_operators={'?',':'}
unary_operators={'+','-','++','--','!'}
bitwise_operators={'~','<<','>>','>>>','^','&'}
data_types={"byte","short","int","long","float","double","boolean","char","class","superclass","object","string","array","interface"," super_interfaces"}
loops_conditions={"for","while","return","if"," method_invocation"}
operators_count=0  #N1

unique_operators=set()
queue = list()
id = 0
queue.append((curr_node, id))

cst_tree.create_node(curr_node.type, id)


while(len(queue)):
    curr_node, parent_id = queue.pop(0)

    for child in curr_node.children:
        id += 1
        queue.append((child, id))
        cst_tree.create_node(child.type, id, parent = parent_id)
print(cst_tree.show(stdout=False))

while(len(queue)):
    curr_node, parent_id = queue.pop(0)
    if(curr_node.type in arethmatc_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in assignment_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in relational_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in logical_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in ternary_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in unary_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in bitwise_operators ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in data_types ):
        unique_operators.add(curr_node.type)
        operators_count+=1
    elif (curr_node.type in loops_conditions ):
        unique_operators.add(curr_node.type)
        operators_count+=1



unique_operators_count=len(unique_operators)  #n1
###print(unique_operators_count)

unique_operands =set()
operands_count=0
operators_count=0
unique_operators=set()
operators = {'+', '-', '*', '/', '%', '&&', '||', '!', '&', '|', '^', '~', '<<', '>>', '>>>', '==', '!=', '<', '>', '<=', '>=', '=', '+=', '-=', '*=', '/=', '%=', '&=', '|=', '^=', '<<=', '>>=', '>>>=', '?'}


curr_node = tree.root_node

queue = list()
queue.append(curr_node)
while(len(queue)):
    curr_node = queue.pop(0)
    ###print(curr_node)
    if(curr_node.type in operators):
        unique_operators.add(curr_node.type)
        operators_count+=1
    queue.extend(curr_node.children)

###########################################     operators   ############################################
#access modfiers??, extends, implments ?? , class ?? , function calls , function definations , 
unique_operators=set()
queue = list()
curr_node = tree.root_node
queue.append(curr_node)
excluded_operators=["}","]",")","import", "package"]
while(len(queue)):
    curr_node = queue.pop(0)
    if (not curr_node.is_named and curr_node.type not in excluded_operators):
        unique_operators.add(curr_node.type)
        operators_count+=1 

    if (curr_node.type == "method_invocation"):
        operators_count+=1
        method_name=source_code[curr_node.children[-2].start_byte:curr_node.children[-2].end_byte]
        unique_operators.add(method_name)

    if (curr_node.type == "type_identifier" and curr_node.parent.type!="type_list"  and  curr_node.parent.type!="superclass"):
        operators_count+=1
        class_name=source_code[curr_node.start_byte:curr_node.end_byte]
        unique_operators.add(class_name)  
    if(curr_node.type == "void_type"):
        operators_count+=1
        unique_operators.add("void_type")

    if(curr_node.type != "import_declaration" and curr_node.type != "package_declaration"):
        queue.extend(curr_node.children)
        
###print(unique_operators)

###########################################     operands   ############################################
# package and import statements are not included in operands
# 
unique_operands =set()
queue = list()
curr_node = tree.root_node
queue.append(curr_node)
while(len(queue)):
    curr_node = queue.pop(0)
    if (curr_node.type == "identifier" and curr_node.parent.type != "method_invocation"):
        var_name=source_code[curr_node.start_byte:curr_node.end_byte]
        unique_operands.add(var_name)
        operands_count+=1
    if (curr_node.type == "string_fragment"):
        unique_operands.add(curr_node.type)
        operands_count+=1
    if ("literal" in curr_node.type and curr_node.type != "string_literal" ):
        value=source_code[curr_node.start_byte:curr_node.end_byte]
        unique_operands.add(value)
        operands_count+=1
    if (curr_node.type == "true" and curr_node.type == "false"):
        unique_operands.add(curr_node.type)
        operands_count+=1
    if (curr_node.type == "type_identifier" and (curr_node.parent.type =="type_list"  or  curr_node.parent.type=="superclass")):
        class_name=source_code[curr_node.start_byte:curr_node.end_byte]
        unique_operands.add(class_name)
        operands_count+=1
    if(curr_node.type != "import_declaration" and curr_node.type != "package_declaration"):
        queue.extend(curr_node.children)

##print(unique_operands)

unique_operators_count=len(unique_operators)  #n1
unique_operands_count=len(unique_operands)  #n2

# Program vocabulary: η=η1+η2
# Program length: N=N1+N2
# Calculated program length: Nˆ=η1log2η1+η2log2η2
# Volume: V=Nlog2η
# Difficulty: D=η12⋅N2η2
# Effort: E=D⋅V
# Time required to program: T=E18 seconds
# Number of delivered bugs: B=V3000.

n=unique_operands_count+unique_operators_count
N=operators_count+operands_count
N_hat=unique_operators_count*math.log2(unique_operators_count)+unique_operands_count*math.log2(unique_operands_count)
V=N*math.log2(n)
D=(unique_operators_count/2)*(operands_count/unique_operands_count)
E=D*V
T=E/18
B=V/3000

# calculate SLOC
SLOC = 0
flag = False
with open("Operation2.java", "r") as file:
    for line in file:
        if line.strip().rstrip('\n')[0:2] == '/*':
            flag = True
        if line.strip().rstrip('\n')[-2:] == '*/': 
            flag = False
            continue
        if flag:
            continue
        if line.strip().rstrip('\n') != "" and not line.strip().startswith(('package', 'import','//')):
            SLOC += 1
##print(SLOC)

    # int x = 4; /* jijopok
    # this is a comment
    # ldkfsl;l */  int y = 5;

################################### Cyclomatic Complexity  ########################################

# if	+1	An if statement is a single decision.
# elif	+1	The elif statement adds another decision.
# else	+0	The else statement does not cause a new decision. The decision is at the if.
# for	+1	There is a decision at the start of the loop.
# while	+1	There is a decision at the while statement.
# except	+1	Each except branch adds a new conditional path of execution.
# finally	+0	The finally block is unconditionally executed.
# with	+1	The with statement roughly corresponds to a try/except block (see PEP 343 for details).
# assert	+1	The assert statement internally roughly equals a conditional statement.
# Comprehension	+1	A list/set/dict comprehension of generator expression is equivalent to a for loop.
# Boolean Operator	+1	Every boolean operator (and, or) adds a decision point.
