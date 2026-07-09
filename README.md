MEJORAS

NUEVOS TDAs

Postulaciones implementadas con Cola con Prioridad y conjuntos: La consigna original pedía procesar las solicitudes de empleo estrictamente por orden de llegada. Esto puede desembocar en que se tengan que procesar las solicitudes de cientos de postulantes poco capacitados solo porque se postularon primero. Por ende, se implementa una Cola con Prioridad. El sistema calcula un puntaje de "match" evaluando la intersección de las habilidades del candidato con los requerimientos del puesto, encolando primero a los perfiles más aptos. Además, se implementa un conjunto para trabajar de manera más eficiente la verificación de que un usuario no se postule dos veces en el mismo puesto.

CAMBIOS EN LOS TDAs

Grafo de Lista de Adyacencia: Anteriormente se usaba un Grafo con Matriz de Adyacencia que reservaba espacio en memoria para una matriz de tamaño V^2 (V: vértices/usuarios). Las redes sociales reales son grafos extremadamente dispersos. Por eso, se migró esta lógica a un Grafo con Listas de Adyacencias no solo para optimizar drásticamente el espacio, sino que también para acelerar los recorridos para las recomendaciones bajando la complejidad temporal a O(V+A) (A: Aristas/conexiones).

REDIMENSIONAMIENTOS

Redimensionamiento Automático en el Diccionario: El DiccionarioUsuarios se inicializaba con una capacidad fija estática (100). Si la red crecía, las listas enlazadas de las colisiones se hacían larguísimas, destruyendo la búsqueda instantánea. Por consiguiente, se implementó una función que calcula el "factor de carga" y duplica dinámicamente el tamaño del arreglo interno cuando supera el 75% de ocupación.

Redimensionamiento Automático en el Grafo: Inicialmente, el Grafo poseía un límite fijo de capacidad para sus vértices (usuarios), lo que impedía que la red social creciera de forma natural una vez alcanzado ese tope, frenando la ejecución del sistema. Para solucionarlo, se implementó una técnica de redimensionamiento dinámico. Ahora, al intentar insertar un usuario que supera la capacidad actual, el sistema automáticamente instancia arreglos del doble de tamaño (tanto para los vértices como para las listas de adyacencia) y transfiere las referencias existentes.
