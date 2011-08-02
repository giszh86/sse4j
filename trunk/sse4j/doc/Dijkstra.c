#include <string.h>
#include <float.h>

/******************************************************************************
/
/ VirtualNetwork structs
/
******************************************************************************/

typedef struct NetworkArcStruct
{
	/* an ARC */
	const struct NetworkNodeStruct *NodeFrom;
	const struct NetworkNodeStruct *NodeTo;
	int ArcRowid;
	double Cost;
} NetworkArc;
typedef NetworkArc *NetworkArcPtr;

typedef struct NetworkNodeStruct
{
	/* a NODE */
	int InternalIndex;
	int Id;
	char *Code;
	int NumArcs;
	NetworkArcPtr Arcs;
} NetworkNode;
typedef NetworkNode *NetworkNodePtr;

typedef struct NetworkStruct
{
	/* the main NETWORK structure */
	int EndianArch;
	int MaxCodeLength;
	int CurrentIndex;
	int NodeCode;
	int NumNodes;
	char *TableName;
	char *FromColumn;
	char *ToColumn;
	char *GeometryColumn;
	NetworkNodePtr Nodes;
} Network;
typedef Network *NetworkPtr;

/******************************************************************************
/
/ Dijkstra structs
/
******************************************************************************/

typedef struct DijkstraNode
{
	int Id;
	struct DijkstraNode **To;
	NetworkArcPtr *Link;
	int DimTo;
	struct DijkstraNode *PreviousNode;
	NetworkArcPtr Arc;
	double Distance;
	int Value;
} DijkstraNode;
typedef DijkstraNode *DijkstraNodePtr;

typedef struct DijkstraNodes
{
	DijkstraNodePtr Nodes;
	int Dim;
	int DimLink;
} DijkstraNodes;
typedef DijkstraNodes *DijkstraNodesPtr;

typedef struct DjikstraHeapStruct
{
	DijkstraNodePtr *Values;
	int Head;
	int Tail;
} DijkstraHeap;
typedef DijkstraHeap *DijkstraHeapPtr;

/******************************************************************************
/
/ Dijkstra implementation
/
******************************************************************************/

static DijkstraNodesPtr
dijkstra_init (NetworkPtr graph)
{
	/* allocating and initializing the Dijkstra struct */
	int i;
	int j;
	DijkstraNodesPtr nd;
	NetworkNodePtr nn;
	/* allocating the main Nodes struct */
	nd = malloc (sizeof (DijkstraNodes));
	/* allocating and initializing  Nodes array */
	nd->Nodes = malloc (sizeof (DijkstraNode) * graph->NumNodes);
	nd->Dim = graph->NumNodes;
	nd->DimLink = 0;
	for (i = 0; i < graph->NumNodes; i++)
	{
		/* initializing the Nodes array */
		nn = graph->Nodes + i;
		nd->Nodes[i].Id = nn->InternalIndex;
		nd->Nodes[i].DimTo = nn->NumArcs;
		nd->Nodes[i].To = malloc (sizeof (DijkstraNodePtr) * nn->NumArcs);
		nd->Nodes[i].Link = malloc (sizeof (NetworkArcPtr) * nn->NumArcs);
		for (j = 0; j < nn->NumArcs; j++)
		{
			/*  setting the outcoming Arcs for the current Node */
			nd->DimLink++;
			nd->Nodes[i].To[j] =
				nd->Nodes + nn->Arcs[j].NodeTo->InternalIndex;
			nd->Nodes[i].Link[j] = nn->Arcs + j;
		}
	}
	return (nd);
}

static void
dijkstra_free (DijkstraNodes * e)
{
	/* memory cleanup; freeing the Dijkstra struct */
	int i;
	for (i = 0; i < e->Dim; i++)
	{
		if (e->Nodes[i].DimTo != 0)
		{
			free (e->Nodes[i].Link);
			free (e->Nodes[i].To);
		}
	}
	free (e->Nodes);
	free (e);
}

static DijkstraHeapPtr
dijkstra_heap_init (int dim)
{
	/* allocating the Nodes ordered list */
	DijkstraHeapPtr h;
	h = malloc (sizeof (DijkstraHeap));
	h->Values = malloc (sizeof (DijkstraNodePtr) * dim);
	h->Head = 0;
	h->Tail = 0;
	return (h);
}

static void
dijkstra_heap_free (DijkstraHeapPtr h)
{
	/* freeing the Nodes ordered list */
	free (h->Values);
	free (h);
}

static int
dijkstra_compare (const void *a, const void *b)
{
	/* comparison function for QSORT */
	return (int) (((DijkstraNodePtr) a)->Distance -
		((DijkstraNodePtr) b)->Distance);
}

static void
dijkstra_push (DijkstraHeapPtr h, DijkstraNodePtr n)
{
	/* inserting a Node into the ordered list */
	h->Values[h->Tail] = n;
	h->Tail++;
}

static DijkstraNodePtr
dijkstra_pop (DijkstraHeapPtr h)
{
	/* fetching the minimum value from the ordered list */
	DijkstraNodePtr n;
	qsort (h->
		Values +
		h->Head,
		h->Tail - h->Head, sizeof (DijkstraNodePtr), dijkstra_compare);
	n = h->Values[h->Head];
	h->Head++;
	return (n);
}

static NetworkArcPtr *
dijkstra_shortest_path (DijkstraNodesPtr e, NetworkNodePtr pfrom,
						NetworkNodePtr pto, int *ll)
{
	/* identifying the Shortest Path */
	int from;
	int to;
	int i;
	int k;
	DijkstraNodePtr n;
	int cnt;
	NetworkArcPtr *result;
	DijkstraHeapPtr h;
	/* setting From/To */
	from = pfrom->InternalIndex;
	to = pto->InternalIndex;
	/* initializing the heap */
	h = dijkstra_heap_init (e->DimLink);
	/* initializing the graph */
	for (i = 0; i < e->Dim; i++)
	{
		e->Nodes[i].PreviousNode = NULL;
		e->Nodes[i].Arc = NULL;
		e->Nodes[i].Value = 0;
		e->Nodes[i].Distance = DBL_MAX;
	}
	/* pushes the From node into the Nodes list */
	e->Nodes[from].Distance = 0.0;
	dijkstra_push (h, e->Nodes + from);
	while (h->Tail != h->Head)
	{
		/* Dijsktra loop */
		n = dijkstra_pop (h);
		if (n->Id == to)
		{
			/* destination reached */
			break;
		}
		n->Value = 1;
		for (i = 0; i < n->DimTo; i++)
		{
			if (n->To[i]->Value == 0)
			{
				if (n->To[i]->Distance > n->Distance + n->Link[i]->Cost)
				{
					n->To[i]->Distance = n->Distance + n->Link[i]->Cost;
					n->To[i]->PreviousNode = n;
					n->To[i]->Arc = n->Link[i];
					dijkstra_push (h, n->To[i]);
				}
			}
		}
	}
	dijkstra_heap_free (h);
	cnt = 0;
	n = e->Nodes + to;
	while (n->PreviousNode != NULL)
	{
		/* counting how many Arcs are into the Shortest Path solution */
		cnt++;
		n = n->PreviousNode;
	}
	/* allocating the solution */
	result = malloc (sizeof (NetworkArcPtr) * cnt);
	k = cnt - 1;
	n = e->Nodes + to;
	while (n->PreviousNode != NULL)
	{
		/* inserting an Arc  into the solution */
		result[k] = n->Arc;
		n = n->PreviousNode;
		k--;
	}
	*ll = cnt;
	return (result);
}
