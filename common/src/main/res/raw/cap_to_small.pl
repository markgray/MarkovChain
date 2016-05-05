#!/usr/bin/perl -w

use strict;

while(<>) {
  chomp;
  my @bits = m{(.)(.*)};
  print lc $_, " ", $bits[0], lc $bits[1], "\n";
}


