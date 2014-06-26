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
The player starts at time-point A1.  This means that the world up
until this point must first be generated.  Ideally, this can
be done with a single seed, as no player input need be considered.

The player plays the game until time-point A2, at which point he dies
and is respawned at time-point B1, which can be generated using the 
initial seed.  Changes made now will affect the future world, but 
this is ignored for the time being, though player input is recorded.

The player dies at time-point B2 and respawns at C1.  This is far more
complicated as we must reconcile 3 timelines.

1. Initial
2. Timeline A
3. Timeline B

	Lazy Timeline Evaluation:
Store all received data.  Record all seeds.
When leaving time-interval A to spawn in time-interval B, where
A is entirely before B, 



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
