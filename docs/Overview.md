Procedural Time Travel Game Overview
====================================

Main packages:
--------------
	Ai
	Core
	Entities
	Gui

Vocabulary:
-----------
Game Instantiation:
	When the player first starts up a new game, a seed is picked and
	world terrain is generated.  A starting number of NPCs are 
	generated 


Core Mechanics:
---------------
	Time Travel:

Keep careful track of random number generation and player input.
We need to generate game behaviour in a deterministic way in order
to be able to play it back.

Imagine the following timeline:

-----B1---B2--A1---C1---C2---A2---

The player starts at timepoint A1.  This means that the world up
until this point must first be generated.  Ideally, this can
be done with a single seed, as no player input need be considered.

The player plays the game until timepoint A2, at which point he dies
and is respawned at timepoint B1, which can be generated using the 
initial seed.  Changes made now will affect the future world, but 
this is ignored for the time being, though player input is recorded.

The player dies at timepoint B2 and respawns at C1.  This is far more
complicated as we must reconcile 3 timelines.

1. Initial
2. Timeline A
3. Timeline B

	Lazy Timeline Evaluation:
Store all received data.  Record all seeds.
When leaving time-interval B to spawn in time-interval C, where
B is entirely before C, replay through the world history starting
from B2, with the player input stored from timeline A applied at the 
appropriate points.  This should result in a world that can start at
timepoint C1.

There is a issue when the recorded player input from timeline A conflicts
with the changes from timelines B and eventually C.  This can be remedied in
various ways, but the current plan is to ignore input after a conflict, and
turn the player into an NPC.  Hopefully we can create a special type of 
'adventurer' npc.


	NPC Behaviour:
NPCs are assigned 'genetic material', which amounts to a series of
seeds and indices.  This will determine how they make decisions.
On game instantiation

	Procedural Generation:


Data Structures:
----------------
	Game Map:
The game map is a 2D array of Tile objects.  This allows for
constant time access of tiles in a given area, which is 
important due to the large map.

Tile objects store references to all objects occupying them in
order to make use of the random access.  As a result, all
locale-based operations can happen in constant time.
