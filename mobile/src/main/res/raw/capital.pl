#!/usr/bin/perl -w

use strict;

open INP, '<king_james_caps_hash.txt';
my %capHash = ();

while (my $line = <INP>) {
  chomp $line;
  my @words = split / /, $line;
  $capHash{$words[0]} = $words[1];
}

close INP;

while (my $line =<>) {
  chomp $line;
  my @words = split / /, $line;
  foreach my $word (@words) {
    my $punct = '';
    if ($word =~ m{([a-z-]+)([.!?])}) {
      $word = $1;
      $punct = $2;
    }
    if (exists $capHash{$word}) {
      $word = $capHash{$word};
    }
    print "$word", "$punct ";
  }
  print "\n";
}


