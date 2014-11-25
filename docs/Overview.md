Procedural Time Travel Game Overview
====================================
Note: This project is very much a work-in-progress.


Main packages:
--------------
* Core:
  * This is the main package containing the entry point into the game (Game.java).
It contains several subpackages as well as the TileMap-related code.  It is also generally
the place where sections of the project live before they get large enough to get moved to their
own packages.
* Core.display:
  * This package handles game-specific display classes such as the Sprite class.
It battles for position with the main Display package a little, and
they may eventually be moved together.
* Core.util:
  * This package handles utility classes that are deemed not overly game-specific.  PerlinNoise.java
and Poly.java are examples.
* Core.path:
  * This handles any pathfinding code used by the game.  Currently the game can pathfind on any space
that implements the Pathable interface.
* Entities:
  * This contains the entities of the game.  It also holds subpackages for abstract and concrete implementations
as well as a subpackage of interfaces.  Currently these interfaces are used mainly to employ the marker interface
pattern to determine how entities can be interacted with.
* AI:
  * This package contains some abstract picking aways at an AI system for the game.
The idea is to be flexible enough to cover monster as well as NPC AI.  This means
that the system needs to be able to handle realtime tasks such as movement and
fighting, as well as long term goal satisfaction such as building a village.  Ideally
the villagers will also be able to be slightly differentiated in the way that they 
approach tasks.
  * Currently Goal Oriented Action Planning (GOAP) is being considered as a solution to this
problem.
* Display:
  * This package contains what attempts to be a fairly game-independent library of graphics-related
classes.  The bulk of it is made up of an assortment of Java Swing-esque GUI elements.  GUtil.java
is quite heavily used to do all the drawing of sprites onto the screen.


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

	Timeline Evaluation:
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
'adventurer' NPC that would fight monsters roughly like the character would have.


	NPC Behaviour:
NPCs are assigned 'genetic material', which amounts to a series of
seeds and indices.  This will determine how they make decisions.

	Procedural Generation:
Perlin Noise is used on game instantiation to generate values for each
map tile.  These values are converted to either grass, dirt, water or sand.
Then a smoothing algorithm is run over the entire map that converts
transition tiles to the correct image for the transition.  For example,
a grass tile left of a sand tile will be changed to a tile sprite that
has grass on the left and smoothly transitions to sand on the right.

Data Structures:
----------------
	Game Map:
The game map is a 2D array of Tile objects.  This allows for
constant time access of tiles in a given area, which is 
important due to the large map.

Tile objects store references to all objects occupying them in
order to make use of the random access.  As a result, all
locale-based operations can happen in constant time.


Vocabulary:
-----------
Game Instantiation:
	When the player first starts up a new game, a seed is picked and
	world terrain is generated.  A starting number of NPCs are 
	generated 

Entity:
	Pretty much anything in the game other than tiles.  Items and moving
	entities such as the player, NPCs or Monsters are good examples.
	Non-tile map objects also fall into this category.
	
NPC:
	Non-Player Character
