# Mejoras Funcionales y Refactorización Arquitectónica

## NUEVOS TDAs

* **Cola con Prioridad para Postulaciones:** La consigna original pedía procesar las solicitudes de empleo estrictamente por orden de llegada (FIFO). Esto desemboca en ineficiencia empresarial, teniendo que procesar cientos de postulantes poco capacitados solo por haberse postulado primero. Se implementó una Cola de Prioridad Estática donde el sistema calcula un "Matching Score" evaluando la intersección de las habilidades del candidato con los requerimientos del puesto, encolando primero a los perfiles más aptos.
* **Conjunto (Set) de Unicidad:** Se diseñó un TDA Conjunto respaldado por una función Hash para trabajar de manera más eficiente la verificación de postulaciones. Al operar en tiempo O(1) constante, el conjunto valida y bloquea automáticamente que un usuario no pueda postularse dos veces al mismo puesto.

## CAMBIOS EN LOS TDAs

* **Grafo de Lista de Adyacencia:** Anteriormente se usaba un Grafo con Matriz de Adyacencia que reservaba espacio en memoria para una matriz estática de tamaño V^2 (V: vértices/usuarios). Las redes sociales reales son grafos extremadamente dispersos. Por eso, se migró esta lógica a un Grafo con Listas de Adyacencias puras (Lista de Listas). Esto no solo optimiza drásticamente el espacio en memoria eliminando las celdas vacías, sino que acelera los recorridos para las recomendaciones y el DFS bajando la complejidad temporal a O(V+A) (A: aristas/conexiones).

## REDIMENSIONAMIENTO Y ESCALABILIDAD

* **Redimensionamiento Automático en el Diccionario:** El `DiccionarioUsuarios` se inicializaba con una capacidad fija estática. Si la red crecía, las listas enlazadas de las colisiones se hacían larguísimas, destruyendo la búsqueda instantánea O(1). Se implementó una función que calcula el "Factor de Carga" y realiza un *Rehashing* (duplica dinámicamente el tamaño del arreglo interno) cuando supera el 75% de ocupación.
* **Memoria 100% Dinámica en el Grafo:** Al modificar el Grafo hacia una estructura pura de "Lista de Listas" enlazadas mediante punteros de memoria, se eliminó por completo la dependencia de arreglos primitivos. El sistema ya no posee un límite máximo de capacidad para la red social; la plataforma escala de manera infinita por su propia naturaleza dinámica.
