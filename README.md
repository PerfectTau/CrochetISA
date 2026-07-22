 # Undergraduate Research Project: Crochet Instruction Set Architecture
 Created by Andi Moss

## Introduction
Current crochet patterns are primarily written for human crocheters to use. This means that most patterns are written at the stitch level, assuming that the crocheter has the knowledge on how to create those stitches. However, these patterns are not detailed enough for crochet machines, which are a growing area of research. This Crochet ISA breaks down stitch level instructions into action level instructions that a machine could theoretically use. Additionally, this program outputs a stitch level connectivity graph, as well as a interactive console program for stepping through the pattern action by action. I call this a loop level visualization, as the building blocks are the loops on the hook, rather than the collection of loops referred to as stitches.
Syntax used is designed to be compatible with Svetlin Tassev's CrochetPARADE program.

## Use

This program takes in a file path to a .txt file that contains the pattern to be translated into the actions required to crochet that pattern. Stitches are separated by commas and each new line is treated as a new row. Turning chains are required by this program.

### Syntax

Stitches implemented:
Chain (ch)
Slip Stitch (ss)
Single Crochet (sc)
Half double Crochet (hdc)
Double Crochet (dc)
Treble Crochet (tr)
Double Treble Crochet (dtr)
Front Loop only (stitch_name + fl)
Back Loop only (stitch_name + bl)
Front Post (fp + stitch_name)
Back Post (bp + stitch_name)
Skip (sk)
Turn (turn)

#### Multiples
A number (x) followed by a stitch name is treated as x stitches. This means that '3sc' is treated the same as 'sc, sc, sc'

