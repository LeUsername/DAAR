TAILLE = 7

# zcat rollernet.dyn.gz| awk '{if($3<=1500&&$3>=1200)if($1<$2)print $1,$2;else print $2,$1}' | sort -k1 -g |uniq > aretes.txt
def adjMatrix(fileName):
    f = open(fileName,"r")
    mat = np.ones((TAILLE, TAILLE ))*math.inf
    f.close()
    f = open(fileName,"r")
    for a in f:
        s=a.split()
        i = int(s[0])
        j = int(s[1])
        mat[i][j] = 1
        mat[j][i] = 1
        mat[i][i] = 0
        mat[j][j] = 0
        
    return mat

def adjMatrix2(fileName):
  global TAILLE
  f = open(fileName,"r")
  mat = list()
  for i in range(0,TAILLE):
    mat.append(list())
    for j in range(0,TAILLE):
      mat[i].append(list())
  f.close()
 
  return mat



def floydWarshall(fileName):
  global TAILLE
  dist = adjMatrix(fileName)
  mat = adjMatrix2(fileName)
  for k in range (0,TAILLE):
    for i in range (0,TAILLE):
      for j in range (0,TAILLE):
        dist[i][j] = min(dist[i][j],dist[i][k]+dist[k][j])
        
  for k in range (0,TAILLE):
    for i in range (0,TAILLE):
      for j in range (0,TAILLE):
        if(math.isinf(dist[i][j])):
          continue
        if(dist[i][j]>=dist[i][k]+dist[k][j]):
          if(dist[i][k]>1):
            continue
          mat[i][j].append(k)
        if(i in mat[i][j]):
          mat[i][j].remove(i)
  return mat
  
f = floydWarshall("a.txt")
for i in range (0,TAILLE):
  print(i, " >> ", f[i])