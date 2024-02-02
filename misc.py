# https://tree-sitter.github.io/tree-sitter/using-parsers
#https://learn.microsoft.com/en-us/visualstudio/code-quality/code-metrics-cyclomatic-complexity?view=vs-2022
#https://radon.readthedocs.io/en/latest/intro.html#:~:text=Maintainability%20Index%20is%20a%20software,Cyclomatic%20Complexity%20and%20Halstead%20volume
#-----------------------javalang-------------------------#
import javalang

with open("dataset/railo.sqlite3/railo-master/railo-java/railo-core/src/railo/runtime/sql/exp/op/Operation1.java", 'r') as file:
	code = file.read()

parsed_tree = javalang.parse.parse(code)

# print(parsed_tree.)
for path, node in parsed_tree:
    if isinstance(node, javalang.tree.ClassDeclaration):
            print(f"Class: {node.name}")

    elif isinstance(node, javalang.tree.MethodDeclaration):
        print(f"  Method: {node.name}")
        
        # Accessing method parameters
        parameters = [param.name for param in node.parameters]
        print(f"    Parameters: {', '.join(parameters)}")

    elif isinstance(node, javalang.tree.VariableDeclarator):
         print(f"    Variable: {node.name}")

#---------------------------------------------------------#
         
print(f"Identifier: {source_code[curr_node.start_byte:curr_node.end_byte]}")



# if curr_node.is_named==False :
    #print(curr_node)