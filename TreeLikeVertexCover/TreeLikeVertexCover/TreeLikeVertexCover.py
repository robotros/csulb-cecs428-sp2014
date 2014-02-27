#
# Vertex Cover Using a advanced search tree and pre-processing.
# algorithm based on UDACITY Course introduction to Theoretical Computer Science
# Aron Roberts
# CECS 428 California State University Long Beach
#


# This function initializes and calls the search tree
def vertex_cover_tree(input_graph):
    n = len(input_graph)
    assignment = [None]*n
    return recursive_vertex_cover(input_graph, assignment)

# This function recursively builds the search tree
def recursive_vertex_cover(input_graph, assignment):
    print (input_graph)
    print (assignment)
    n = len(input_graph)
    v = -1
    u = -1

    for i in range(n):
        for j in range(i,n):
            if input_graph[i][j] ==1:
                if assignment[i] == 0 and assignment[j] == 0:
                    return float("inf")
                elif assignment[i] == None and assignment[j] == None:
                    u = i
                    v = j

    if v == -1:
        size = 0
        for i in range(n):
            if assignment[i] == 1:
                size += 1
            elif assignment[i] == None:
                for j in range(i,n):
                    if input_graph[i][j] == 1:
                        if assignment[j] == 0:
                            size += 1     
                            
        print (assignment)                                           
        return size

    # End of your code. The following code  takes care of the recursive
    # branching. Do not modify anything below here!
    assignment[u] = 1
    assignment[v] = 0
    size_10 = recursive_vertex_cover(input_graph, assignment)
    assignment[u] = 0
    assignment[v] = 1
    size_01 = recursive_vertex_cover(input_graph, assignment)
    assignment[u] = 1
    assignment[v] = 1
    size_11 = recursive_vertex_cover(input_graph, assignment)
    assignment[u] = None
    assignment[v] = None
    min_size = min(size_10, size_01, size_11)
    return min_size




def test():
    assert 1 == vertex_cover_tree([[0, 1],
                                   [1, 0]])
    print (vertex_cover_tree([[0, 1, 1],
                                   [1, 1, 0],
                                   [1, 0, 0]]))


test()