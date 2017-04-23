#!/usr/bin/perl -w
# -*- mode: cperl;-*-

use strict;

sub handAngles(@) {
  my ($hour, $minute, $second) = @_;
  my @angle = ();

  $angle[0] = 6.0 * $second;
  $angle[1] = 6.0 * ($minute + $second/60.0);
  $angle[2] = 30.0 * ($hour + $minute/60.0 + $second/3600.0);
  if ($angle[2]>360.0) {
    $angle[2] -= 360.0;
  }

  return @angle;
}

my %rankings = ();

for my $hour (1..12) {
  for my $minute (0..59) {
    for (my $second=0.0; $second<60.0; $second+=1.0) {
      my $clock = sprintf "%02i:%02i:%06.3f", $hour, $minute, $second;
      my @angle = sort {$a<=>$b} handAngles $hour, $minute, $second;
      my @slices = sort {$b<=>$a} ($angle[1]-$angle[0],
                                   $angle[2]-$angle[1],
                                   $angle[0]-$angle[2]+360.0);

      my $niceness = abs($slices[0]-120.0) +
                     abs($slices[1]-120.0) +
                     abs($slices[2]-120.0);

      print "$clock ($slices[0], $slices[1], $slices[2]) $niceness\n";
      $rankings{$clock} = $niceness;
    }
  }
}

my @rankings = sort {$rankings{$a}<=>$rankings{$b}} keys %rankings;

print "Beginning of rankings:\n";

for my $clock (@rankings) {
  print "$clock $rankings{$clock}\n";
}

