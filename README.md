 # Undergraduate Research Project: Crochet Instruction Set Architecture
 Created by Andi Moss

## Introduction
Current crochet patterns are primarily written for human crocheters to use. This means that most patterns are written at the stitch level, assuming that the crocheter has the knowledge on how to create those stitches. However, these patterns are not detailed enough for crochet machines, which are a growing area of research. This Crochet ISA breaks down stitch level instructions into action level instructions that a machine could theoretically use. Additionally, this program outputs a stitch level connectivity graph, as well as a interactive console program for stepping through the pattern action by action. I call this a loop level visualization, as the building blocks are the loops on the hook, rather than the collection of loops referred to as stitches.
Syntax used is designed to be compatible with Svetlin Tassev's CrochetPARADE program.

## Use

This program takes in a file path to a .txt file that contains the pattern to be translated into the actions required to crochet that pattern. Stitches are separated by commas and each new line is treated as a new row. Turning chains are required by this program.

### Syntax
Actions:
* YO: yarn over. Used to add a loop to the hook
* PT: pull through. Pulls the last loop on the hook through the second to last loop
* INSERT: inserts the hook into a previous stitch
* MOVE: sets the next attach point to the next stitch in the crocheting direction
* TURN: ends the current row, setting the next stitch to the beginning of the next row, and turns the crocheting direction
* SK: skip. Skips the next attach point

  
| Stitch Name/Command | Description | Actions |
| --------------------| ------------| --------|
| ch | Chain Stitch. Used as the foundational row of many crochet patterns and to generate width along a row| YO, PT |
| ss | Slip Stitch. Used to join rounds or move along stitches without adding any height. | INSERT, YO, PT, PT |
| sc | Single Crochet. Creates a tight fabric. | INSERT, YO, PT, YO, PT, PT |
| hdc | Half Double Crochet. Creates a slightly taller stitch. | YO, INSERT, YO, PT, YO, PT, PT, PT |
| dc | Double Crochet. Creates a tall stitch and a lighter fabric. | YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT |
| tr | Treble Crochet. Requires a turning chain of 3 | YO, YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT, YO, PT, PT |
| dtr | Double Treble Crochet. Requires a turning chain of 4 | YO, YO, YO, INSERT, YO, PT, YO, PT, PT, YO, PT, PT, YO, PT, PT, YO, PT, PT |
| ...fl (e.g. scfl, trfl) | Front Loop Insertion point. | Inserts into the front top loop |
| ...bl (e.g. hdcbl, ssbl) | Back Loop Insertion point | Inserts into the back top loop |
| fp... (e.g. fpdc, fpdtr) | Front Post Insertion point | Inserts from the front of the fabric around the post of the stitch to be attached to |
| bp... (e.g. bphdc, bptr) | Back Post Insertion point | Inserts from the back of the fabric around the post of the stitch to be attached to |
| sk | Skip. | Skips next attach point |
| turn | Turn. | Turns working direction and starts a new row|


#### Multiples
A number (x) followed by a stitch name is treated as x stitches. This means that '3sc' is treated the same as 'sc, sc, sc'

