package com.example.android.markovchain.shakespeare

/**
 * Shakespeare's complete sonnets
 */
object ShakespeareSonnets {

    /**
     * Our data
     */
    val SONNETS: Array<String> = arrayOf(
        "From fairest creatures we desire increase,\n" +
            "That thereby beauty's rose might never die,\n" +
            "But as the riper should by time decease,\n" +
            "His tender heir might bear his memory:\n" +
            "But thou, contracted to thine own bright eyes,\n" +
            "Feed'st thy light's flame with self-substantial fuel,\n" +
            "Making a famine where abundance lies,\n" +
            "Thy self thy foe, to thy sweet self too cruel:\n" +
            "Thou that art now the world's fresh ornament,\n" +
            "And only herald to the gaudy spring,\n" +
            "Within thine own bud buriest thy content,\n" +
            "And tender churl mak'st waste in niggarding:\n" +
            "  Pity the world, or else this glutton be,\n" +
            "  To eat the world's due, by the grave and thee. ",

        "When forty winters shall besiege thy brow,\n" +
            "And dig deep trenches in thy beauty's field,\n" +
            "Thy youth's proud livery so gazed on now,\n" +
            "Will be a tatter'd weed of small worth held:\n" +
            "Then being asked, where all thy beauty lies,\n" +
            "Where all the treasure of thy lusty days;\n" +
            "To say, within thine own deep sunken eyes,\n" +
            "Were an all-eating shame, and thriftless praise.\n" +
            "How much more praise deserv'd thy beauty's use,\n" +
            "If thou couldst answer 'This fair child of mine\n" +
            "Shall sum my count, and make my old excuse,'\n" +
            "Proving his beauty by succession thine!\n" +
            "  This were to be new made when thou art old,\n" +
            "  And see thy blood warm when thou feel'st it cold. ",

        "Look in thy glass and tell the face thou viewest\n" +
            "Now is the time that face should form another;\n" +
            "Whose fresh repair if now thou not renewest,\n" +
            "Thou dost beguile the world, unbless some mother.\n" +
            "For where is she so fair whose unear'd womb\n" +
            "Disdains the tillage of thy husbandry?\n" +
            "Or who is he so fond will be the tomb,\n" +
            "Of his self-love to stop posterity?\n" +
            "Thou art thy mother's glass and she in thee\n" +
            "Calls back the lovely April of her prime;\n" +
            "So thou through windows of thine age shalt see,\n" +
            "Despite of wrinkles this thy golden time.\n" +
            "  But if thou live, remember'd not to be,\n" +
            "  Die single and thine image dies with thee. ",

        "Unthrifty loveliness, why dost thou spend\n" +
            "Upon thy self thy beauty's legacy?\n" +
            "Nature's bequest gives nothing, but doth lend,\n" +
            "And being frank she lends to those are free:\n" +
            "Then, beauteous niggard, why dost thou abuse\n" +
            "The bounteous largess given thee to give?\n" +
            "Profitless usurer, why dost thou use\n" +
            "So great a sum of sums, yet canst not live?\n" +
            "For having traffic with thy self alone,\n" +
            "Thou of thy self thy sweet self dost deceive:\n" +
            "Then how when nature calls thee to be gone,\n" +
            "What acceptable audit canst thou leave?\n" +
            "  Thy unused beauty must be tombed with thee,\n" +
            "  Which, used, lives th' executor to be. ",

        "Those hours, that with gentle work did frame\n" +
            "The lovely gaze where every eye doth dwell,\n" +
            "Will play the tyrants to the very same\n" +
            "And that unfair which fairly doth excel;\n" +
            "For never-resting time leads summer on\n" +
            "To hideous winter, and confounds him there;\n" +
            "Sap checked with frost, and lusty leaves quite gone,\n" +
            "Beauty o'er-snowed and bareness every where:\n" +
            "Then were not summer's distillation left,\n" +
            "A liquid prisoner pent in walls of glass,\n" +
            "Beauty's effect with beauty were bereft,\n" +
            "Nor it, nor no remembrance what it was:\n" +
            "  But flowers distill'd, though they with winter meet,\n" +
            "  Leese but their show; their substance still lives sweet. ",

        "Then let not winter's ragged hand deface,\n" +
            "In thee thy summer, ere thou be distill'd:\n" +
            "Make sweet some vial; treasure thou some place\n" +
            "With beauty's treasure ere it be self-kill'd.\n" +
            "That use is not forbidden usury,\n" +
            "Which happies those that pay the willing loan;\n" +
            "That's for thy self to breed another thee,\n" +
            "Or ten times happier, be it ten for one;\n" +
            "Ten times thy self were happier than thou art,\n" +
            "If ten of thine ten times refigur'd thee:\n" +
            "Then what could death do if thou shouldst depart,\n" +
            "Leaving thee living in posterity?\n" +
            "  Be not self-will'd, for thou art much too fair\n" +
            "  To be death's conquest and make worms thine heir. ",

        "Lo! in the orient when the gracious light\n" +
            "Lifts up his burning head, each under eye\n" +
            "Doth homage to his new-appearing sight,\n" +
            "Serving with looks his sacred majesty;\n" +
            "And having climb'd the steep-up heavenly hill,\n" +
            "Resembling strong youth in his middle age,\n" +
            "Yet mortal looks adore his beauty still,\n" +
            "Attending on his golden pilgrimage:\n" +
            "But when from highmost pitch, with weary car,\n" +
            "Like feeble age, he reeleth from the day,\n" +
            "The eyes, 'fore duteous, now converted are\n" +
            "From his low tract, and look another way:\n" +
            "  So thou, thyself outgoing in thy noon:\n" +
            "  Unlook'd, on diest unless thou get a son. ",

        "Music to hear, why hear'st thou music sadly?\n" +
            "Sweets with sweets war not, joy delights in joy:\n" +
            "Why lov'st thou that which thou receiv'st not gladly,\n" +
            "Or else receiv'st with pleasure thine annoy?\n" +
            "If the true concord of well-tuned sounds,\n" +
            "By unions married, do offend thine ear,\n" +
            "They do but sweetly chide thee, who confounds\n" +
            "In singleness the parts that thou shouldst bear.\n" +
            "Mark how one string, sweet husband to another,\n" +
            "Strikes each in each by mutual ordering;\n" +
            "Resembling sire and child and happy mother,\n" +
            "Who, all in one, one pleasing note do sing:\n" +
            "  Whose speechless song being many, seeming one,\n" +
            "  Sings this to thee: 'Thou single wilt prove none.' ",

        "Is it for fear to wet a widow's eye,\n" +
            "That thou consum'st thy self in single life?\n" +
            "Ah! if thou issueless shalt hap to die,\n" +
            "The world will wail thee like a makeless wife;\n" +
            "The world will be thy widow and still weep\n" +
            "That thou no form of thee hast left behind,\n" +
            "When every private widow well may keep\n" +
            "By children's eyes, her husband's shape in mind:\n" +
            "Look! what an unthrift in the world doth spend\n" +
            "Shifts but his place, for still the world enjoys it;\n" +
            "But beauty's waste hath in the world an end,\n" +
            "And kept unused the user so destroys it.\n" +
            "  No love toward others in that bosom sits\n" +
            "  That on himself such murd'rous shame commits. ",

        "For shame! deny that thou bear'st love to any,\n" +
            "Who for thy self art so unprovident.\n" +
            "Grant, if thou wilt, thou art belov'd of many,\n" +
            "But that thou none lov'st is most evident:\n" +
            "For thou art so possess'd with murderous hate,\n" +
            "That 'gainst thy self thou stick'st not to conspire,\n" +
            "Seeking that beauteous roof to ruinate\n" +
            "Which to repair should be thy chief desire.\n" +
            "O! change thy thought, that I may change my mind:\n" +
            "Shall hate be fairer lodg'd than gentle love?\n" +
            "Be, as thy presence is, gracious and kind,\n" +
            "Or to thyself at least kind-hearted prove:\n" +
            "  Make thee another self for love of me,\n" +
            "  That beauty still may live in thine or thee. ",

        "As fast as thou shalt wane, so fast thou grow'st,\n" +
            "In one of thine, from that which thou departest;\n" +
            "And that fresh blood which youngly thou bestow'st,\n" +
            "Thou mayst call thine when thou from youth convertest,\n" +
            "Herein lives wisdom, beauty, and increase;\n" +
            "Without this folly, age, and cold decay:\n" +
            "If all were minded so, the times should cease\n" +
            "And threescore year would make the world away.\n" +
            "Let those whom nature hath not made for store,\n" +
            "Harsh, featureless, and rude, barrenly perish:\n" +
            "Look, whom she best endow'd, she gave thee more;\n" +
            "Which bounteous gift thou shouldst in bounty cherish:\n" +
            "  She carv'd thee for her seal, and meant thereby,\n" +
            "  Thou shouldst print more, not let that copy die. ",

        "When I do count the clock that tells the time,\n" +
            "And see the brave day sunk in hideous night;\n" +
            "When I behold the violet past prime,\n" +
            "And sable curls, all silvered o'er with white;\n" +
            "When lofty trees I see barren of leaves,\n" +
            "Which erst from heat did canopy the herd,\n" +
            "And summer's green all girded up in sheaves,\n" +
            "Borne on the bier with white and bristly beard,\n" +
            "Then of thy beauty do I question make,\n" +
            "That thou among the wastes of time must go,\n" +
            "Since sweets and beauties do themselves forsake\n" +
            "And die as fast as they see others grow;\n" +
            "  And nothing 'gainst Time's scythe can make defence\n" +
            "  Save breed, to brave him when he takes thee hence. ",

        "O! that you were your self; but, love you are\n" +
            "No longer yours, than you your self here live:\n" +
            "Against this coming end you should prepare,\n" +
            "And your sweet semblance to some other give:\n" +
            "So should that beauty which you hold in lease\n" +
            "Find no determination; then you were\n" +
            "Yourself again, after yourself's decease,\n" +
            "When your sweet issue your sweet form should bear.\n" +
            "Who lets so fair a house fall to decay,\n" +
            "Which husbandry in honour might uphold,\n" +
            "Against the stormy gusts of winter's day\n" +
            "And barren rage of death's eternal cold?\n" +
            "  O! none but unthrifts. Dear my love, you know,\n" +
            "  You had a father: let your son say so. ",

        "Not from the stars do I my judgement pluck;\n" +
            "And yet methinks I have astronomy,\n" +
            "But not to tell of good or evil luck,\n" +
            "Of plagues, of dearths, or seasons' quality;\n" +
            "Nor can I fortune to brief minutes tell,\n" +
            "Pointing to each his thunder, rain and wind,\n" +
            "Or say with princes if it shall go well\n" +
            "By oft predict that I in heaven find:\n" +
            "But from thine eyes my knowledge I derive,\n" +
            "And constant stars in them I read such art\n" +
            "As 'Truth and beauty shall together thrive,\n" +
            "If from thyself, to store thou wouldst convert';\n" +
            "  Or else of thee this I prognosticate:\n" +
            "  'Thy end is truth's and beauty's doom and date.' ",

        "When I consider every thing that grows\n" +
            "Holds in perfection but a little moment,\n" +
            "That this huge stage presenteth nought but shows\n" +
            "Whereon the stars in secret influence comment;\n" +
            "When I perceive that men as plants increase,\n" +
            "Cheered and checked even by the self-same sky,\n" +
            "Vaunt in their youthful sap, at height decrease,\n" +
            "And wear their brave state out of memory;\n" +
            "Then the conceit of this inconstant stay\n" +
            "Sets you most rich in youth before my sight,\n" +
            "Where wasteful Time debateth with decay\n" +
            "To change your day of youth to sullied night,\n" +
            "  And all in war with Time for love of you,\n" +
            "  As he takes from you, I engraft you new. ",

        "But wherefore do not you a mightier way\n" +
            "Make war upon this bloody tyrant, Time?\n" +
            "And fortify your self in your decay\n" +
            "With means more blessed than my barren rhyme?\n" +
            "Now stand you on the top of happy hours,\n" +
            "And many maiden gardens, yet unset,\n" +
            "With virtuous wish would bear you living flowers,\n" +
            "Much liker than your painted counterfeit:\n" +
            "So should the lines of life that life repair,\n" +
            "Which this, Time's pencil, or my pupil pen,\n" +
            "Neither in inward worth nor outward fair,\n" +
            "Can make you live your self in eyes of men.\n" +
            "  To give away yourself, keeps yourself still,\n" +
            "  And you must live, drawn by your own sweet skill. ",

        "Who will believe my verse in time to come,\n" +
            "If it were fill'd with your most high deserts?\n" +
            "Though yet heaven knows it is but as a tomb\n" +
            "Which hides your life, and shows not half your parts.\n" +
            "If I could write the beauty of your eyes,\n" +
            "And in fresh numbers number all your graces,\n" +
            "The age to come would say 'This poet lies;\n" +
            "Such heavenly touches ne'er touch'd earthly faces.'\n" +
            "So should my papers, yellow'd with their age,\n" +
            "Be scorn'd, like old men of less truth than tongue,\n" +
            "And your true rights be term'd a poet's rage\n" +
            "And stretched metre of an antique song:\n" +
            "  But were some child of yours alive that time,\n" +
            "  You should live twice,--in it, and in my rhyme. ",

        "Shall I compare thee to a summer's day?\n" +
            "Thou art more lovely and more temperate:\n" +
            "Rough winds do shake the darling buds of May,\n" +
            "And summer's lease hath all too short a date:\n" +
            "Sometime too hot the eye of heaven shines,\n" +
            "And often is his gold complexion dimm'd,\n" +
            "And every fair from fair sometime declines,\n" +
            "By chance, or nature's changing course untrimm'd:\n" +
            "But thy eternal summer shall not fade,\n" +
            "Nor lose possession of that fair thou ow'st,\n" +
            "Nor shall death brag thou wander'st in his shade,\n" +
            "When in eternal lines to time thou grow'st,\n" +
            "  So long as men can breathe, or eyes can see,\n" +
            "  So long lives this, and this gives life to thee. ",

        "Devouring Time, blunt thou the lion's paws,\n" +
            "And make the earth devour her own sweet brood;\n" +
            "Pluck the keen teeth from the fierce tiger's jaws,\n" +
            "And burn the long-liv'd phoenix, in her blood;\n" +
            "Make glad and sorry seasons as thou fleets,\n" +
            "And do whate'er thou wilt, swift-footed Time,\n" +
            "To the wide world and all her fading sweets;\n" +
            "But I forbid thee one most heinous crime:\n" +
            "O! carve not with thy hours my love's fair brow,\n" +
            "Nor draw no lines there with thine antique pen;\n" +
            "Him in thy course untainted do allow\n" +
            "For beauty's pattern to succeeding men.\n" +
            "  Yet, do thy worst old Time: despite thy wrong,\n" +
            "  My love shall in my verse ever live young. ",

        "A woman's face with nature's own hand painted,\n" +
            "Hast thou, the master mistress of my passion;\n" +
            "A woman's gentle heart, but not acquainted\n" +
            "With shifting change, as is false women's fashion:\n" +
            "An eye more bright than theirs, less false in rolling,\n" +
            "Gilding the object whereupon it gazeth;\n" +
            "A man in hue all 'hues' in his controlling,\n" +
            "Which steals men's eyes and women's souls amazeth.\n" +
            "And for a woman wert thou first created;\n" +
            "Till Nature, as she wrought thee, fell a-doting,\n" +
            "And by addition me of thee defeated,\n" +
            "By adding one thing to my purpose nothing.\n" +
            "  But since she prick'd thee out for women's pleasure,\n" +
            "  Mine be thy love and thy love's use their treasure. ",

        "So is it not with me as with that Muse,\n" +
            "Stirr'd by a painted beauty to his verse,\n" +
            "Who heaven itself for ornament doth use\n" +
            "And every fair with his fair doth rehearse,\n" +
            "Making a couplement of proud compare'\n" +
            "With sun and moon, with earth and sea's rich gems,\n" +
            "With April's first-born flowers, and all things rare,\n" +
            "That heaven's air in this huge rondure hems.\n" +
            "O! let me, true in love, but truly write,\n" +
            "And then believe me, my love is as fair\n" +
            "As any mother's child, though not so bright\n" +
            "As those gold candles fix'd in heaven's air:\n" +
            "  Let them say more that like of hearsay well;\n" +
            "  I will not praise that purpose not to sell. ",

        "My glass shall not persuade me I am old,\n" +
            "So long as youth and thou are of one date;\n" +
            "But when in thee time's furrows I behold,\n" +
            "Then look I death my days should expiate.\n" +
            "For all that beauty that doth cover thee,\n" +
            "Is but the seemly raiment of my heart,\n" +
            "Which in thy breast doth live, as thine in me:\n" +
            "How can I then be elder than thou art?\n" +
            "O! therefore love, be of thyself so wary\n" +
            "As I, not for myself, but for thee will;\n" +
            "Bearing thy heart, which I will keep so chary\n" +
            "As tender nurse her babe from faring ill.\n" +
            "  Presume not on thy heart when mine is slain,\n" +
            "  Thou gav'st me thine not to give back again. ",

        "As an unperfect actor on the stage,\n" +
            "Who with his fear is put beside his part,\n" +
            "Or some fierce thing replete with too much rage,\n" +
            "Whose strength's abundance weakens his own heart;\n" +
            "So I, for fear of trust, forget to say\n" +
            "The perfect ceremony of love's rite,\n" +
            "And in mine own love's strength seem to decay,\n" +
            "O'ercharg'd with burthen of mine own love's might.\n" +
            "O! let my looks be then the eloquence\n" +
            "And dumb presagers of my speaking breast,\n" +
            "Who plead for love, and look for recompense,\n" +
            "More than that tongue that more hath more express'd.\n" +
            "  O! learn to read what silent love hath writ:\n" +
            "  To hear with eyes belongs to love's fine wit. ",

        "Mine eye hath play'd the painter and hath stell'd,\n" +
            "Thy beauty's form in table of my heart;\n" +
            "My body is the frame wherein 'tis held,\n" +
            "And perspective it is best painter's art.\n" +
            "For through the painter must you see his skill,\n" +
            "To find where your true image pictur'd lies,\n" +
            "Which in my bosom's shop is hanging still,\n" +
            "That hath his windows glazed with thine eyes.\n" +
            "Now see what good turns eyes for eyes have done:\n" +
            "Mine eyes have drawn thy shape, and thine for me\n" +
            "Are windows to my breast, where-through the sun\n" +
            "Delights to peep, to gaze therein on thee;\n" +
            "  Yet eyes this cunning want to grace their art,\n" +
            "  They draw but what they see, know not the heart. ",

        "Let those who are in favour with their stars\n" +
            "Of public honour and proud titles boast,\n" +
            "Whilst I, whom fortune of such triumph bars\n" +
            "Unlook'd for joy in that I honour most.\n" +
            "Great princes' favourites their fair leaves spread\n" +
            "But as the marigold at the sun's eye,\n" +
            "And in themselves their pride lies buried,\n" +
            "For at a frown they in their glory die.\n" +
            "The painful warrior famoused for fight,\n" +
            "After a thousand victories once foil'd,\n" +
            "Is from the book of honour razed quite,\n" +
            "And all the rest forgot for which he toil'd:\n" +
            "Then happy I, that love and am belov'd,\n" +
            "Where I may not remove nor be remov'd. ",

        "Lord of my love, to whom in vassalage\n" +
            "Thy merit hath my duty strongly knit,\n" +
            "To thee I send this written embassage,\n" +
            "To witness duty, not to show my wit:\n" +
            "Duty so great, which wit so poor as mine\n" +
            "May make seem bare, in wanting words to show it,\n" +
            "But that I hope some good conceit of thine\n" +
            "In thy soul's thought, all naked, will bestow it:\n" +
            "Till whatsoever star that guides my moving,\n" +
            "Points on me graciously with fair aspect,\n" +
            "And puts apparel on my tatter'd loving,\n" +
            "To show me worthy of thy sweet respect:\n" +
            "  Then may I dare to boast how I do love thee;\n" +
            "  Till then, not show my head where thou mayst prove me. ",

        "Weary with toil, I haste me to my bed,\n" +
            "The dear respose for limbs with travel tir'd;\n" +
            "But then begins a journey in my head\n" +
            "To work my mind, when body's work's expired:\n" +
            "For then my thoughts--from far where I abide--\n" +
            "Intend a zealous pilgrimage to thee,\n" +
            "And keep my drooping eyelids open wide,\n" +
            "Looking on darkness which the blind do see:\n" +
            "Save that my soul's imaginary sight\n" +
            "Presents thy shadow to my sightless view,\n" +
            "Which, like a jewel (hung in ghastly night,\n" +
            "Makes black night beauteous, and her old face new.\n" +
            "  Lo! thus, by day my limbs, by night my mind,\n" +
            "  For thee, and for myself, no quiet find. ",

        "How can I then return in happy plight,\n" +
            "That am debarre'd the benefit of rest?\n" +
            "When day's oppression is not eas'd by night,\n" +
            "But day by night and night by day oppress'd,\n" +
            "And each, though enemies to either's reign,\n" +
            "Do in consent shake hands to torture me,\n" +
            "The one by toil, the other to complain\n" +
            "How far I toil, still farther off from thee.\n" +
            "I tell the day, to please him thou art bright,\n" +
            "And dost him grace when clouds do blot the heaven:\n" +
            "So flatter I the swart-complexion'd night,\n" +
            "When sparkling stars twire not thou gild'st the even.\n" +
            "  But day doth daily draw my sorrows longer,\n" +
            "  And night doth nightly make grief's length seem stronger. ",

        "When in disgrace with fortune and men's eyes\n" +
            "I all alone beweep my outcast state,\n" +
            "And trouble deaf heaven with my bootless cries,\n" +
            "And look upon myself, and curse my fate,\n" +
            "Wishing me like to one more rich in hope,\n" +
            "Featur'd like him, like him with friends possess'd,\n" +
            "Desiring this man's art, and that man's scope,\n" +
            "With what I most enjoy contented least;\n" +
            "Yet in these thoughts my self almost despising,\n" +
            "Haply I think on thee,-- and then my state,\n" +
            "Like to the lark at break of day arising\n" +
            "From sullen earth, sings hymns at heaven's gate;\n" +
            "  For thy sweet love remember'd such wealth brings\n" +
            "  That then I scorn to change my state with kings. ",

        "When to the sessions of sweet silent thought\n" +
            "I summon up remembrance of things past,\n" +
            "I sigh the lack of many a thing I sought,\n" +
            "And with old woes new wail my dear time's waste:\n" +
            "Then can I drown an eye, unused to flow,\n" +
            "For precious friends hid in death's dateless night,\n" +
            "And weep afresh love's long since cancell'd woe,\n" +
            "And moan the expense of many a vanish'd sight:\n" +
            "Then can I grieve at grievances foregone,\n" +
            "And heavily from woe to woe tell o'er\n" +
            "The sad account of fore-bemoaned moan,\n" +
            "Which I new pay as if not paid before.\n" +
            "  But if the while I think on thee, dear friend,\n" +
            "  All losses are restor'd and sorrows end. ",

        "Thy bosom is endeared with all hearts,\n" +
            "Which I by lacking have supposed dead;\n" +
            "And there reigns Love, and all Love's loving parts,\n" +
            "And all those friends which I thought buried.\n" +
            "How many a holy and obsequious tear\n" +
            "Hath dear religious love stol'n from mine eye,\n" +
            "As interest of the dead, which now appear\n" +
            "But things remov'd that hidden in thee lie!\n" +
            "Thou art the grave where buried love doth live,\n" +
            "Hung with the trophies of my lovers gone,\n" +
            "Who all their parts of me to thee did give,\n" +
            "That due of many now is thine alone:\n" +
            "  Their images I lov'd, I view in thee,\n" +
            "  And thou--all they--hast all the all of me. ",

        "If thou survive my well-contented day,\n" +
            "When that churl Death my bones with dust shall cover\n" +
            "And shalt by fortune once more re-survey\n" +
            "These poor rude lines of thy deceased lover,\n" +
            "Compare them with the bett'ring of the time,\n" +
            "And though they be outstripp'd by every pen,\n" +
            "Reserve them for my love, not for their rhyme,\n" +
            "Exceeded by the height of happier men.\n" +
            "O! then vouchsafe me but this loving thought:\n" +
            "'Had my friend's Muse grown with this growing age,\n" +
            "A dearer birth than this his love had brought,\n" +
            "To march in ranks of better equipage:\n" +
            "  But since he died and poets better prove,\n" +
            "  Theirs for their style I'll read, his for his love'. ",

        "Full many a glorious morning have I seen\n" +
            "Flatter the mountain tops with sovereign eye,\n" +
            "Kissing with golden face the meadows green,\n" +
            "Gilding pale streams with heavenly alchemy;\n" +
            "Anon permit the basest clouds to ride\n" +
            "With ugly rack on his celestial face,\n" +
            "And from the forlorn world his visage hide,\n" +
            "Stealing unseen to west with this disgrace:\n" +
            "Even so my sun one early morn did shine,\n" +
            "With all triumphant splendour on my brow;\n" +
            "But out! alack! he was but one hour mine,\n" +
            "The region cloud hath mask'd him from me now.\n" +
            "  Yet him for this my love no whit disdaineth;\n" +
            "  Suns of the world may stain when heaven's sun staineth. ",

        "Why didst thou promise such a beauteous day,\n" +
            "And make me travel forth without my cloak,\n" +
            "To let base clouds o'ertake me in my way,\n" +
            "Hiding thy bravery in their rotten smoke?\n" +
            "'Tis not enough that through the cloud thou break,\n" +
            "To dry the rain on my storm-beaten face,\n" +
            "For no man well of such a salve can speak,\n" +
            "That heals the wound, and cures not the disgrace:\n" +
            "Nor can thy shame give physic to my grief;\n" +
            "Though thou repent, yet I have still the loss:\n" +
            "The offender's sorrow lends but weak relief\n" +
            "To him that bears the strong offence's cross.\n" +
            "  Ah! but those tears are pearl which thy love sheds,\n" +
            "  And they are rich and ransom all ill deeds. ",

        "No more be griev'd at that which thou hast done:\n" +
            "Roses have thorns, and silver fountains mud:\n" +
            "Clouds and eclipses stain both moon and sun,\n" +
            "And loathsome canker lives in sweetest bud.\n" +
            "All men make faults, and even I in this,\n" +
            "Authorizing thy trespass with compare,\n" +
            "Myself corrupting, salving thy amiss,\n" +
            "Excusing thy sins more than thy sins are;\n" +
            "For to thy sensual fault I bring in sense,--\n" +
            "Thy adverse party is thy advocate,--\n" +
            "And 'gainst myself a lawful plea commence:\n" +
            "Such civil war is in my love and hate,\n" +
            "  That I an accessary needs must be,\n" +
            "  To that sweet thief which sourly robs from me. ",

        "Let me confess that we two must be twain,\n" +
            "Although our undivided loves are one:\n" +
            "So shall those blots that do with me remain,\n" +
            "Without thy help, by me be borne alone.\n" +
            "In our two loves there is but one respect,\n" +
            "Though in our lives a separable spite,\n" +
            "Which though it alter not love's sole effect,\n" +
            "Yet doth it steal sweet hours from love's delight.\n" +
            "I may not evermore acknowledge thee,\n" +
            "Lest my bewailed guilt should do thee shame,\n" +
            "Nor thou with public kindness honour me,\n" +
            "Unless thou take that honour from thy name:\n" +
            "  But do not so, I love thee in such sort,\n" +
            "  As thou being mine, mine is thy good report. ",

        "As a decrepit father takes delight\n" +
            "To see his active child do deeds of youth,\n" +
            "So I, made lame by Fortune's dearest spite,\n" +
            "Take all my comfort of thy worth and truth;\n" +
            "For whether beauty, birth, or wealth, or wit,\n" +
            "Or any of these all, or all, or more,\n" +
            "Entitled in thy parts, do crowned sit,\n" +
            "I make my love engrafted, to this store:\n" +
            "So then I am not lame, poor, nor despis'd,\n" +
            "Whilst that this shadow doth such substance give\n" +
            "That I in thy abundance am suffic'd,\n" +
            "And by a part of all thy glory live.\n" +
            "  Look what is best, that best I wish in thee:\n" +
            "  This wish I have; then ten times happy me! ",

        "How can my muse want subject to invent,\n" +
            "While thou dost breathe, that pour'st into my verse\n" +
            "Thine own sweet argument, too excellent\n" +
            "For every vulgar paper to rehearse?\n" +
            "O! give thy self the thanks, if aught in me\n" +
            "Worthy perusal stand against thy sight;\n" +
            "For who's so dumb that cannot write to thee,\n" +
            "When thou thy self dost give invention light?\n" +
            "Be thou the tenth Muse, ten times more in worth\n" +
            "Than those old nine which rhymers invocate;\n" +
            "And he that calls on thee, let him bring forth\n" +
            "Eternal numbers to outlive long date.\n" +
            "  If my slight muse do please these curious days,\n" +
            "  The pain be mine, but thine shall be the praise. ",

        "O! how thy worth with manners may I sing,\n" +
            "When thou art all the better part of me?\n" +
            "What can mine own praise to mine own self bring?\n" +
            "And what is't but mine own when I praise thee?\n" +
            "Even for this, let us divided live,\n" +
            "And our dear love lose name of single one,\n" +
            "That by this separation I may give\n" +
            "That due to thee which thou deserv'st alone.\n" +
            "O absence! what a torment wouldst thou prove,\n" +
            "Were it not thy sour leisure gave sweet leave,\n" +
            "To entertain the time with thoughts of love,\n" +
            "Which time and thoughts so sweetly doth deceive,\n" +
            "  And that thou teachest how to make one twain,\n" +
            "  By praising him here who doth hence remain. ",

        "Take all my loves, my love, yea take them all;\n" +
            "What hast thou then more than thou hadst before?\n" +
            "No love, my love, that thou mayst true love call;\n" +
            "All mine was thine, before thou hadst this more.\n" +
            "Then, if for my love, thou my love receivest,\n" +
            "I cannot blame thee, for my love thou usest;\n" +
            "But yet be blam'd, if thou thy self deceivest\n" +
            "By wilful taste of what thyself refusest.\n" +
            "I do forgive thy robbery, gentle thief,\n" +
            "Although thou steal thee all my poverty:\n" +
            "And yet, love knows it is a greater grief\n" +
            "To bear love's wrong, than hate's known injury.\n" +
            "  Lascivious grace, in whom all ill well shows,\n" +
            "  Kill me with spites yet we must not be foes. ",

        "Those pretty wrongs that liberty commits,\n" +
            "When I am sometime absent from thy heart,\n" +
            "Thy beauty, and thy years full well befits,\n" +
            "For still temptation follows where thou art.\n" +
            "Gentle thou art, and therefore to be won,\n" +
            "Beauteous thou art, therefore to be assail'd;\n" +
            "And when a woman woos, what woman's son\n" +
            "Will sourly leave her till he have prevail'd?\n" +
            "Ay me! but yet thou mightst my seat forbear,\n" +
            "And chide thy beauty and thy straying youth,\n" +
            "Who lead thee in their riot even there\n" +
            "Where thou art forced to break a twofold truth:--\n" +
            "  Hers by thy beauty tempting her to thee,\n" +
            "  Thine by thy beauty being false to me. ",

        "That thou hast her it is not all my grief,\n" +
            "And yet it may be said I loved her dearly;\n" +
            "That she hath thee is of my wailing chief,\n" +
            "A loss in love that touches me more nearly.\n" +
            "Loving offenders thus I will excuse ye:\n" +
            "Thou dost love her, because thou know'st I love her;\n" +
            "And for my sake even so doth she abuse me,\n" +
            "Suffering my friend for my sake to approve her.\n" +
            "If I lose thee, my loss is my love's gain,\n" +
            "And losing her, my friend hath found that loss;\n" +
            "Both find each other, and I lose both twain,\n" +
            "And both for my sake lay on me this cross:\n" +
            "  But here's the joy; my friend and I are one;\n" +
            "  Sweet flattery! then she loves but me alone. ",

        "When most I wink, then do mine eyes best see,\n" +
            "For all the day they view things unrespected;\n" +
            "But when I sleep, in dreams they look on thee,\n" +
            "And darkly bright, are bright in dark directed.\n" +
            "Then thou, whose shadow shadows doth make bright,\n" +
            "How would thy shadow's form form happy show\n" +
            "To the clear day with thy much clearer light,\n" +
            "When to unseeing eyes thy shade shines so!\n" +
            "How would, I say, mine eyes be blessed made\n" +
            "By looking on thee in the living day,\n" +
            "When in dead night thy fair imperfect shade\n" +
            "Through heavy sleep on sightless eyes doth stay!\n" +
            "  All days are nights to see till I see thee,\n" +
            "  And nights bright days when dreams do show thee me. ",

        "If the dull substance of my flesh were thought,\n" +
            "Injurious distance should not stop my way;\n" +
            "For then despite of space I would be brought,\n" +
            "From limits far remote, where thou dost stay.\n" +
            "No matter then although my foot did stand\n" +
            "Upon the farthest earth remov'd from thee;\n" +
            "For nimble thought can jump both sea and land,\n" +
            "As soon as think the place where he would be.\n" +
            "But, ah! thought kills me that I am not thought,\n" +
            "To leap large lengths of miles when thou art gone,\n" +
            "But that so much of earth and water wrought,\n" +
            "I must attend time's leisure with my moan;\n" +
            "  Receiving nought by elements so slow\n" +
            "  But heavy tears, badges of either's woe. ",

        "The other two, slight air, and purging fire\n" +
            "Are both with thee, wherever I abide;\n" +
            "The first my thought, the other my desire,\n" +
            "These present-absent with swift motion slide.\n" +
            "For when these quicker elements are gone\n" +
            "In tender embassy of love to thee,\n" +
            "My life, being made of four, with two alone\n" +
            "Sinks down to death, oppress'd with melancholy;\n" +
            "Until life's composition be recur'd\n" +
            "By those swift messengers return'd from thee,\n" +
            "Who even but now come back again, assur'd,\n" +
            "Of thy fair health, recounting it to me:\n" +
            "  This told, I joy; but then no longer glad,\n" +
            "  I send them back again, and straight grow sad. ",

        "Mine eye and heart are at a mortal war,\n" +
            "How to divide the conquest of thy sight;\n" +
            "Mine eye my heart thy picture's sight would bar,\n" +
            "My heart mine eye the freedom of that right.\n" +
            "My heart doth plead that thou in him dost lie,--\n" +
            "A closet never pierc'd with crystal eyes--\n" +
            "But the defendant doth that plea deny,\n" +
            "And says in him thy fair appearance lies.\n" +
            "To side this title is impannelled\n" +
            "A quest of thoughts, all tenants to the heart;\n" +
            "And by their verdict is determined\n" +
            "The clear eye's moiety, and the dear heart's part:\n" +
            "  As thus; mine eye's due is thy outward part,\n" +
            "  And my heart's right, thy inward love of heart. ",

        "Betwixt mine eye and heart a league is took,\n" +
            "And each doth good turns now unto the other:\n" +
            "When that mine eye is famish'd for a look,\n" +
            "Or heart in love with sighs himself doth smother,\n" +
            "With my love's picture then my eye doth feast,\n" +
            "And to the painted banquet bids my heart;\n" +
            "Another time mine eye is my heart's guest,\n" +
            "And in his thoughts of love doth share a part:\n" +
            "So, either by thy picture or my love,\n" +
            "Thy self away, art present still with me;\n" +
            "For thou not farther than my thoughts canst move,\n" +
            "And I am still with them, and they with thee;\n" +
            "  Or, if they sleep, thy picture in my sight\n" +
            "  Awakes my heart, to heart's and eye's delight. ",

        "How careful was I when I took my way,\n" +
            "Each trifle under truest bars to thrust,\n" +
            "That to my use it might unused stay\n" +
            "From hands of falsehood, in sure wards of trust!\n" +
            "But thou, to whom my jewels trifles are,\n" +
            "Most worthy comfort, now my greatest grief,\n" +
            "Thou best of dearest, and mine only care,\n" +
            "Art left the prey of every vulgar thief.\n" +
            "Thee have I not lock'd up in any chest,\n" +
            "Save where thou art not, though I feel thou art,\n" +
            "Within the gentle closure of my breast,\n" +
            "From whence at pleasure thou mayst come and part;\n" +
            "  And even thence thou wilt be stol'n I fear,\n" +
            "  For truth proves thievish for a prize so dear. ",

        "Against that time, if ever that time come,\n" +
            "When I shall see thee frown on my defects,\n" +
            "When as thy love hath cast his utmost sum,\n" +
            "Call'd to that audit by advis'd respects;\n" +
            "Against that time when thou shalt strangely pass,\n" +
            "And scarcely greet me with that sun, thine eye,\n" +
            "When love, converted from the thing it was,\n" +
            "Shall reasons find of settled gravity;\n" +
            "Against that time do I ensconce me here,\n" +
            "Within the knowledge of mine own desert,\n" +
            "And this my hand, against my self uprear,\n" +
            "To guard the lawful reasons on thy part:\n" +
            "  To leave poor me thou hast the strength of laws,\n" +
            "  Since why to love I can allege no cause. ",

        "How heavy do I journey on the way,\n" +
            "When what I seek, my weary travel's end,\n" +
            "Doth teach that ease and that repose to say,\n" +
            "'Thus far the miles are measured from thy friend!'\n" +
            "The beast that bears me, tired with my woe,\n" +
            "Plods dully on, to bear that weight in me,\n" +
            "As if by some instinct the wretch did know\n" +
            "His rider lov'd not speed, being made from thee:\n" +
            "The bloody spur cannot provoke him on,\n" +
            "That sometimes anger thrusts into his hide,\n" +
            "Which heavily he answers with a groan,\n" +
            "More sharp to me than spurring to his side;\n" +
            "  For that same groan doth put this in my mind,\n" +
            "  My grief lies onward, and my joy behind. ",

        "Thus can my love excuse the slow offence\n" +
            "Of my dull bearer when from thee I speed:\n" +
            "From where thou art why should I haste me thence?\n" +
            "Till I return, of posting is no need.\n" +
            "O! what excuse will my poor beast then find,\n" +
            "When swift extremity can seem but slow?\n" +
            "Then should I spur, though mounted on the wind,\n" +
            "In winged speed no motion shall I know,\n" +
            "Then can no horse with my desire keep pace;\n" +
            "Therefore desire, of perfect'st love being made,\n" +
            "Shall neigh--no dull flesh--in his fiery race;\n" +
            "But love, for love, thus shall excuse my jade,--\n" +
            "  'Since from thee going, he went wilful-slow,\n" +
            "  Towards thee I'll run, and give him leave to go.' ",

        "So am I as the rich, whose blessed key,\n" +
            "Can bring him to his sweet up-locked treasure,\n" +
            "The which he will not every hour survey,\n" +
            "For blunting the fine point of seldom pleasure.\n" +
            "Therefore are feasts so solemn and so rare,\n" +
            "Since, seldom coming in that long year set,\n" +
            "Like stones of worth they thinly placed are,\n" +
            "Or captain jewels in the carcanet.\n" +
            "So is the time that keeps you as my chest,\n" +
            "Or as the wardrobe which the robe doth hide,\n" +
            "To make some special instant special-blest,\n" +
            "By new unfolding his imprison'd pride.\n" +
            "  Blessed are you whose worthiness gives scope,\n" +
            "  Being had, to triumph; being lacked, to hope. ",

        "What is your substance, whereof are you made,\n" +
            "That millions of strange shadows on you tend?\n" +
            "Since every one, hath every one, one shade,\n" +
            "And you but one, can every shadow lend.\n" +
            "Describe Adonis, and the counterfeit\n" +
            "Is poorly imitated after you;\n" +
            "On Helen's cheek all art of beauty set,\n" +
            "And you in Grecian tires are painted new:\n" +
            "Speak of the spring, and foison of the year,\n" +
            "The one doth shadow of your beauty show,\n" +
            "The other as your bounty doth appear;\n" +
            "And you in every blessed shape we know.\n" +
            "  In all external grace you have some part,\n" +
            "  But you like none, none you, for constant heart. ",

        "O! how much more doth beauty beauteous seem\n" +
            "By that sweet ornament which truth doth give.\n" +
            "The rose looks fair, but fairer we it deem\n" +
            "For that sweet odour, which doth in it live.\n" +
            "The canker blooms have full as deep a dye\n" +
            "As the perfumed tincture of the roses.\n" +
            "Hang on such thorns, and play as wantonly\n" +
            "When summer's breath their masked buds discloses:\n" +
            "But, for their virtue only is their show,\n" +
            "They live unwoo'd, and unrespected fade;\n" +
            "Die to themselves. Sweet roses do not so;\n" +
            "Of their sweet deaths, are sweetest odours made:\n" +
            "  And so of you, beauteous and lovely youth,\n" +
            "  When that shall vade, by verse distills your truth. ",

        "Not marble, nor the gilded monuments\n" +
            "Of princes, shall outlive this powerful rhyme;\n" +
            "But you shall shine more bright in these contents\n" +
            "Than unswept stone, besmear'd with sluttish time.\n" +
            "When wasteful war shall statues overturn,\n" +
            "And broils root out the work of masonry,\n" +
            "Nor Mars his sword, nor war's quick fire shall burn\n" +
            "The living record of your memory.\n" +
            "'Gainst death, and all-oblivious enmity\n" +
            "Shall you pace forth; your praise shall still find room\n" +
            "Even in the eyes of all posterity\n" +
            "That wear this world out to the ending doom.\n" +
            "  So, till the judgment that yourself arise,\n" +
            "  You live in this, and dwell in lovers' eyes. ",

        "Sweet love, renew thy force; be it not said\n" +
            "Thy edge should blunter be than appetite,\n" +
            "Which but to-day by feeding is allay'd,\n" +
            "To-morrow sharpened in his former might:\n" +
            "So, love, be thou, although to-day thou fill\n" +
            "Thy hungry eyes, even till they wink with fulness,\n" +
            "To-morrow see again, and do not kill\n" +
            "The spirit of love, with a perpetual dulness.\n" +
            "Let this sad interim like the ocean be\n" +
            "Which parts the shore, where two contracted new\n" +
            "Come daily to the banks, that when they see\n" +
            "Return of love, more blest may be the view;\n" +
            "  Or call it winter, which being full of care,\n" +
            "  Makes summer's welcome, thrice more wished, more rare. ",

        "Being your slave what should I do but tend,\n" +
            "Upon the hours, and times of your desire?\n" +
            "I have no precious time at all to spend;\n" +
            "Nor services to do, till you require.\n" +
            "Nor dare I chide the world-without-end hour,\n" +
            "Whilst I, my sovereign, watch the clock for you,\n" +
            "Nor think the bitterness of absence sour,\n" +
            "When you have bid your servant once adieu;\n" +
            "Nor dare I question with my jealous thought\n" +
            "Where you may be, or your affairs suppose,\n" +
            "But, like a sad slave, stay and think of nought\n" +
            "Save, where you are, how happy you make those.\n" +
            "  So true a fool is love, that in your will,\n" +
            "  Though you do anything, he thinks no ill. ",

        "That god forbid, that made me first your slave,\n" +
            "I should in thought control your times of pleasure,\n" +
            "Or at your hand the account of hours to crave,\n" +
            "Being your vassal, bound to stay your leisure!\n" +
            "O! let me suffer, being at your beck,\n" +
            "The imprison'd absence of your liberty;\n" +
            "And patience, tame to sufferance, bide each check,\n" +
            "Without accusing you of injury.\n" +
            "Be where you list, your charter is so strong\n" +
            "That you yourself may privilage your time\n" +
            "To what you will; to you it doth belong\n" +
            "Yourself to pardon of self-doing crime.\n" +
            "  I am to wait, though waiting so be hell,\n" +
            "  Not blame your pleasure be it ill or well. ",

        "If there be nothing new, but that which is\n" +
            "Hath been before, how are our brains beguil'd,\n" +
            "Which labouring for invention bear amiss\n" +
            "The second burthen of a former child!\n" +
            "O! that record could with a backward look,\n" +
            "Even of five hundred courses of the sun,\n" +
            "Show me your image in some antique book,\n" +
            "Since mind at first in character was done!\n" +
            "That I might see what the old world could say\n" +
            "To this composed wonder of your frame;\n" +
            "Wh'r we are mended, or wh'r better they,\n" +
            "Or whether revolution be the same.\n" +
            "  O! sure I am the wits of former days,\n" +
            "  To subjects worse have given admiring praise. ",

        "Like as the waves make towards the pebbled shore,\n" +
            "So do our minutes hasten to their end;\n" +
            "Each changing place with that which goes before,\n" +
            "In sequent toil all forwards do contend.\n" +
            "Nativity, once in the main of light,\n" +
            "Crawls to maturity, wherewith being crown'd,\n" +
            "Crooked eclipses 'gainst his glory fight,\n" +
            "And Time that gave doth now his gift confound.\n" +
            "Time doth transfix the flourish set on youth\n" +
            "And delves the parallels in beauty's brow,\n" +
            "Feeds on the rarities of nature's truth,\n" +
            "And nothing stands but for his scythe to mow:\n" +
            "  And yet to times in hope, my verse shall stand.\n" +
            "  Praising thy worth, despite his cruel hand. ",

        "Is it thy will, thy image should keep open\n" +
            "My heavy eyelids to the weary night?\n" +
            "Dost thou desire my slumbers should be broken,\n" +
            "While shadows like to thee do mock my sight?\n" +
            "Is it thy spirit that thou send'st from thee\n" +
            "So far from home into my deeds to pry,\n" +
            "To find out shames and idle hours in me,\n" +
            "The scope and tenure of thy jealousy?\n" +
            "O, no! thy love, though much, is not so great:\n" +
            "It is my love that keeps mine eye awake:\n" +
            "Mine own true love that doth my rest defeat,\n" +
            "To play the watchman ever for thy sake:\n" +
            "  For thee watch I, whilst thou dost wake elsewhere,\n" +
            "  From me far off, with others all too near. ",

        "Sin of self-love possesseth all mine eye\n" +
            "And all my soul, and all my every part;\n" +
            "And for this sin there is no remedy,\n" +
            "It is so grounded inward in my heart.\n" +
            "Methinks no face so gracious is as mine,\n" +
            "No shape so true, no truth of such account;\n" +
            "And for myself mine own worth do define,\n" +
            "As I all other in all worths surmount.\n" +
            "But when my glass shows me myself indeed\n" +
            "Beated and chopp'd with tanned antiquity,\n" +
            "Mine own self-love quite contrary I read;\n" +
            "Self so self-loving were iniquity.\n" +
            "  'Tis thee,--myself,--that for myself I praise,\n" +
            "  Painting my age with beauty of thy days. ",

        "Against my love shall be as I am now,\n" +
            "With Time's injurious hand crush'd and o'erworn;\n" +
            "When hours have drain'd his blood and fill'd his brow\n" +
            "With lines and wrinkles; when his youthful morn\n" +
            "Hath travell'd on to age's steepy night;\n" +
            "And all those beauties whereof now he's king\n" +
            "Are vanishing, or vanished out of sight,\n" +
            "Stealing away the treasure of his spring;\n" +
            "For such a time do I now fortify\n" +
            "Against confounding age's cruel knife,\n" +
            "That he shall never cut from memory\n" +
            "My sweet love's beauty, though my lover's life:\n" +
            "  His beauty shall in these black lines be seen,\n" +
            "  And they shall live, and he in them still green. ",

        "When I have seen by Time's fell hand defac'd\n" +
            "The rich-proud cost of outworn buried age;\n" +
            "When sometime lofty towers I see down-raz'd,\n" +
            "And brass eternal slave to mortal rage;\n" +
            "When I have seen the hungry ocean gain\n" +
            "Advantage on the kingdom of the shore,\n" +
            "And the firm soil win of the watery main,\n" +
            "Increasing store with loss, and loss with store;\n" +
            "When I have seen such interchange of state,\n" +
            "Or state itself confounded, to decay;\n" +
            "Ruin hath taught me thus to ruminate--\n" +
            "That Time will come and take my love away.\n" +
            "  This thought is as a death which cannot choose\n" +
            "  But weep to have, that which it fears to lose. ",

        "Since brass, nor stone, nor earth, nor boundless sea,\n" +
            "But sad mortality o'ersways their power,\n" +
            "How with this rage shall beauty hold a plea,\n" +
            "Whose action is no stronger than a flower?\n" +
            "O! how shall summer's honey breath hold out,\n" +
            "Against the wrackful siege of battering days,\n" +
            "When rocks impregnable are not so stout,\n" +
            "Nor gates of steel so strong but Time decays?\n" +
            "O fearful meditation! where, alack,\n" +
            "Shall Time's best jewel from Time's chest lie hid?\n" +
            "Or what strong hand can hold his swift foot back?\n" +
            "Or who his spoil of beauty can forbid?\n" +
            "  O! none, unless this miracle have might,\n" +
            "  That in black ink my love may still shine bright. ",

        "Tired with all these, for restful death I cry,\n" +
            "As to behold desert a beggar born,\n" +
            "And needy nothing trimm'd in jollity,\n" +
            "And purest faith unhappily forsworn,\n" +
            "And gilded honour shamefully misplac'd,\n" +
            "And maiden virtue rudely strumpeted,\n" +
            "And right perfection wrongfully disgrac'd,\n" +
            "And strength by limping sway disabled\n" +
            "And art made tongue-tied by authority,\n" +
            "And folly--doctor-like--controlling skill,\n" +
            "And simple truth miscall'd simplicity,\n" +
            "And captive good attending captain ill:\n" +
            "  Tir'd with all these, from these would I be gone,\n" +
            "  Save that, to die, I leave my love alone. ",

        "Ah! wherefore with infection should he live,\n" +
            "And with his presence grace impiety,\n" +
            "That sin by him advantage should achieve,\n" +
            "And lace itself with his society?\n" +
            "Why should false painting imitate his cheek,\n" +
            "And steel dead seeming of his living hue?\n" +
            "Why should poor beauty indirectly seek\n" +
            "Roses of shadow, since his rose is true?\n" +
            "Why should he live, now Nature bankrupt is,\n" +
            "Beggar'd of blood to blush through lively veins?\n" +
            "For she hath no exchequer now but his,\n" +
            "And proud of many, lives upon his gains.\n" +
            "  O! him she stores, to show what wealth she had\n" +
            "  In days long since, before these last so bad. ",

        "Thus is his cheek the map of days outworn,\n" +
            "When beauty lived and died as flowers do now,\n" +
            "Before these bastard signs of fair were born,\n" +
            "Or durst inhabit on a living brow;\n" +
            "Before the golden tresses of the dead,\n" +
            "The right of sepulchres, were shorn away,\n" +
            "To live a second life on second head;\n" +
            "Ere beauty's dead fleece made another gay:\n" +
            "In him those holy antique hours are seen,\n" +
            "Without all ornament, itself and true,\n" +
            "Making no summer of another's green,\n" +
            "Robbing no old to dress his beauty new;\n" +
            "  And him as for a map doth Nature store,\n" +
            "  To show false Art what beauty was of yore. ",

        "Those parts of thee that the world's eye doth view\n" +
            "Want nothing that the thought of hearts can mend;\n" +
            "All tongues--the voice of souls--give thee that due,\n" +
            "Uttering bare truth, even so as foes commend.\n" +
            "Thy outward thus with outward praise is crown'd;\n" +
            "But those same tongues, that give thee so thine own,\n" +
            "In other accents do this praise confound\n" +
            "By seeing farther than the eye hath shown.\n" +
            "They look into the beauty of thy mind,\n" +
            "And that in guess they measure by thy deeds;\n" +
            "Then--churls--their thoughts, although their eyes were kind,\n" +
            "To thy fair flower add the rank smell of weeds:\n" +
            "  But why thy odour matcheth not thy show,\n" +
            "  The soil is this, that thou dost common grow. ",

        "That thou art blam'd shall not be thy defect,\n" +
            "For slander's mark was ever yet the fair;\n" +
            "The ornament of beauty is suspect,\n" +
            "A crow that flies in heaven's sweetest air.\n" +
            "So thou be good, slander doth but approve\n" +
            "Thy worth the greater being woo'd of time;\n" +
            "For canker vice the sweetest buds doth love,\n" +
            "And thou present'st a pure unstained prime.\n" +
            "Thou hast passed by the ambush of young days\n" +
            "Either not assail'd, or victor being charg'd;\n" +
            "Yet this thy praise cannot be so thy praise,\n" +
            "To tie up envy, evermore enlarg'd,\n" +
            "  If some suspect of ill mask'd not thy show,\n" +
            "  Then thou alone kingdoms of hearts shouldst owe. ",

        "No longer mourn for me when I am dead\n" +
            "Than you shall hear the surly sullen bell\n" +
            "Give warning to the world that I am fled\n" +
            "From this vile world with vilest worms to dwell:\n" +
            "Nay, if you read this line, remember not\n" +
            "The hand that writ it, for I love you so,\n" +
            "That I in your sweet thoughts would be forgot,\n" +
            "If thinking on me then should make you woe.\n" +
            "O! if,--I say you look upon this verse,\n" +
            "When I perhaps compounded am with clay,\n" +
            "Do not so much as my poor name rehearse;\n" +
            "But let your love even with my life decay;\n" +
            "  Lest the wise world should look into your moan,\n" +
            "  And mock you with me after I am gone. ",

        "O! lest the world should task you to recite\n" +
            "What merit lived in me, that you should love\n" +
            "After my death,--dear love, forget me quite,\n" +
            "For you in me can nothing worthy prove;\n" +
            "Unless you would devise some virtuous lie,\n" +
            "To do more for me than mine own desert,\n" +
            "And hang more praise upon deceased I\n" +
            "Than niggard truth would willingly impart:\n" +
            "O! lest your true love may seem false in this\n" +
            "That you for love speak well of me untrue,\n" +
            "My name be buried where my body is,\n" +
            "And live no more to shame nor me nor you.\n" +
            "  For I am shamed by that which I bring forth,\n" +
            "  And so should you, to love things nothing worth. ",

        "That time of year thou mayst in me behold\n" +
            "When yellow leaves, or none, or few, do hang\n" +
            "Upon those boughs which shake against the cold,\n" +
            "Bare ruin'd choirs, where late the sweet birds sang.\n" +
            "In me thou see'st the twilight of such day\n" +
            "As after sunset fadeth in the west;\n" +
            "Which by and by black night doth take away,\n" +
            "Death's second self, that seals up all in rest.\n" +
            "In me thou see'st the glowing of such fire,\n" +
            "That on the ashes of his youth doth lie,\n" +
            "As the death-bed, whereon it must expire,\n" +
            "Consum'd with that which it was nourish'd by.\n" +
            "  This thou perceiv'st, which makes thy love more strong,\n" +
            "  To love that well, which thou must leave ere long. ",

        "But be contented: when that fell arrest\n" +
            "Without all bail shall carry me away,\n" +
            "My life hath in this line some interest,\n" +
            "Which for memorial still with thee shall stay.\n" +
            "When thou reviewest this, thou dost review\n" +
            "The very part was consecrate to thee:\n" +
            "The earth can have but earth, which is his due;\n" +
            "My spirit is thine, the better part of me:\n" +
            "So then thou hast but lost the dregs of life,\n" +
            "The prey of worms, my body being dead;\n" +
            "The coward conquest of a wretch's knife,\n" +
            "Too base of thee to be remembered.\n" +
            "  The worth of that is that which it contains,\n" +
            "  And that is this, and this with thee remains. ",

        "So are you to my thoughts as food to life,\n" +
            "Or as sweet-season'd showers are to the ground;\n" +
            "And for the peace of you I hold such strife\n" +
            "As 'twixt a miser and his wealth is found.\n" +
            "Now proud as an enjoyer, and anon\n" +
            "Doubting the filching age will steal his treasure;\n" +
            "Now counting best to be with you alone,\n" +
            "Then better'd that the world may see my pleasure:\n" +
            "Sometime all full with feasting on your sight,\n" +
            "And by and by clean starved for a look;\n" +
            "Possessing or pursuing no delight,\n" +
            "Save what is had, or must from you be took.\n" +
            "  Thus do I pine and surfeit day by day,\n" +
            "  Or gluttoning on all, or all away. ",

        "Why is my verse so barren of new pride,\n" +
            "So far from variation or quick change?\n" +
            "Why with the time do I not glance aside\n" +
            "To new-found methods, and to compounds strange?\n" +
            "Why write I still all one, ever the same,\n" +
            "And keep invention in a noted weed,\n" +
            "That every word doth almost tell my name,\n" +
            "Showing their birth, and where they did proceed?\n" +
            "O! know sweet love I always write of you,\n" +
            "And you and love are still my argument;\n" +
            "So all my best is dressing old words new,\n" +
            "Spending again what is already spent:\n" +
            "  For as the sun is daily new and old,\n" +
            "  So is my love still telling what is told. ",

        "Thy glass will show thee how thy beauties wear,\n" +
            "Thy dial how thy precious minutes waste;\n" +
            "These vacant leaves thy mind's imprint will bear,\n" +
            "And of this book, this learning mayst thou taste.\n" +
            "The wrinkles which thy glass will truly show\n" +
            "Of mouthed graves will give thee memory;\n" +
            "Thou by thy dial's shady stealth mayst know\n" +
            "Time's thievish progress to eternity.\n" +
            "Look! what thy memory cannot contain,\n" +
            "Commit to these waste blanks, and thou shalt find\n" +
            "Those children nursed, deliver'd from thy brain,\n" +
            "To take a new acquaintance of thy mind.\n" +
            "  These offices, so oft as thou wilt look,\n" +
            "  Shall profit thee and much enrich thy book. ",

        "So oft have I invoked thee for my Muse,\n" +
            "And found such fair assistance in my verse\n" +
            "As every alien pen hath got my use\n" +
            "And under thee their poesy disperse.\n" +
            "Thine eyes, that taught the dumb on high to sing\n" +
            "And heavy ignorance aloft to fly,\n" +
            "Have added feathers to the learned's wing\n" +
            "And given grace a double majesty.\n" +
            "Yet be most proud of that which I compile,\n" +
            "Whose influence is thine, and born of thee:\n" +
            "In others' works thou dost but mend the style,\n" +
            "And arts with thy sweet graces graced be;\n" +
            "  But thou art all my art, and dost advance\n" +
            "  As high as learning, my rude ignorance. ",

        "Whilst I alone did call upon thy aid,\n" +
            "My verse alone had all thy gentle grace;\n" +
            "But now my gracious numbers are decay'd,\n" +
            "And my sick Muse doth give an other place.\n" +
            "I grant, sweet love, thy lovely argument\n" +
            "Deserves the travail of a worthier pen;\n" +
            "Yet what of thee thy poet doth invent\n" +
            "He robs thee of, and pays it thee again.\n" +
            "He lends thee virtue, and he stole that word\n" +
            "From thy behaviour; beauty doth he give,\n" +
            "And found it in thy cheek: he can afford\n" +
            "No praise to thee, but what in thee doth live.\n" +
            "  Then thank him not for that which he doth say,\n" +
            "  Since what he owes thee, thou thyself dost pay. ",

        "O! how I faint when I of you do write,\n" +
            "Knowing a better spirit doth use your name,\n" +
            "And in the praise thereof spends all his might,\n" +
            "To make me tongue-tied speaking of your fame!\n" +
            "But since your worth--wide as the ocean is,--\n" +
            "The humble as the proudest sail doth bear,\n" +
            "My saucy bark, inferior far to his,\n" +
            "On your broad main doth wilfully appear.\n" +
            "Your shallowest help will hold me up afloat,\n" +
            "Whilst he upon your soundless deep doth ride;\n" +
            "Or, being wrack'd, I am a worthless boat,\n" +
            "He of tall building, and of goodly pride:\n" +
            "  Then if he thrive and I be cast away,\n" +
            "  The worst was this,--my love was my decay. ",

        "Or I shall live your epitaph to make,\n" +
            "Or you survive when I in earth am rotten;\n" +
            "From hence your memory death cannot take,\n" +
            "Although in me each part will be forgotten.\n" +
            "Your name from hence immortal life shall have,\n" +
            "Though I, once gone, to all the world must die:\n" +
            "The earth can yield me but a common grave,\n" +
            "When you entombed in men's eyes shall lie.\n" +
            "Your monument shall be my gentle verse,\n" +
            "Which eyes not yet created shall o'er-read;\n" +
            "And tongues to be, your being shall rehearse,\n" +
            "When all the breathers of this world are dead;\n" +
            "  You still shall live,--such virtue hath my pen,--\n" +
            "  Where breath most breathes, even in the mouths of men. ",

        "I grant thou wert not married to my Muse,\n" +
            "And therefore mayst without attaint o'erlook\n" +
            "The dedicated words which writers use\n" +
            "Of their fair subject, blessing every book.\n" +
            "Thou art as fair in knowledge as in hue,\n" +
            "Finding thy worth a limit past my praise;\n" +
            "And therefore art enforced to seek anew\n" +
            "Some fresher stamp of the time-bettering days.\n" +
            "And do so, love; yet when they have devis'd,\n" +
            "What strained touches rhetoric can lend,\n" +
            "Thou truly fair, wert truly sympathiz'd\n" +
            "In true plain words, by thy true-telling friend;\n" +
            "  And their gross painting might be better us'd\n" +
            "  Where cheeks need blood; in thee it is abus'd. ",

        "I never saw that you did painting need,\n" +
            "And therefore to your fair no painting set;\n" +
            "I found, or thought I found, you did exceed\n" +
            "That barren tender of a poet's debt:\n" +
            "And therefore have I slept in your report,\n" +
            "That you yourself, being extant, well might show\n" +
            "How far a modern quill doth come too short,\n" +
            "Speaking of worth, what worth in you doth grow.\n" +
            "This silence for my sin you did impute,\n" +
            "Which shall be most my glory being dumb;\n" +
            "For I impair not beauty being mute,\n" +
            "When others would give life, and bring a tomb.\n" +
            "  There lives more life in one of your fair eyes\n" +
            "  Than both your poets can in praise devise. ",

        "Who is it that says most, which can say more,\n" +
            "Than this rich praise,--that you alone, are you?\n" +
            "In whose confine immured is the store\n" +
            "Which should example where your equal grew.\n" +
            "Lean penury within that pen doth dwell\n" +
            "That to his subject lends not some small glory;\n" +
            "But he that writes of you, if he can tell\n" +
            "That you are you, so dignifies his story,\n" +
            "Let him but copy what in you is writ,\n" +
            "Not making worse what nature made so clear,\n" +
            "And such a counterpart shall fame his wit,\n" +
            "Making his style admired every where.\n" +
            "  You to your beauteous blessings add a curse,\n" +
            "  Being fond on praise, which makes your praises worse. ",

        "My tongue-tied Muse in manners holds her still,\n" +
            "While comments of your praise richly compil'd,\n" +
            "Reserve their character with golden quill,\n" +
            "And precious phrase by all the Muses fil'd.\n" +
            "I think good thoughts, whilst others write good words,\n" +
            "And like unlettered clerk still cry 'Amen'\n" +
            "To every hymn that able spirit affords,\n" +
            "In polish'd form of well-refined pen.\n" +
            "Hearing you praised, I say ''tis so, 'tis true,'\n" +
            "And to the most of praise add something more;\n" +
            "But that is in my thought, whose love to you,\n" +
            "Though words come hindmost, holds his rank before.\n" +
            "  Then others, for the breath of words respect,\n" +
            "  Me for my dumb thoughts, speaking in effect. ",

        "Was it the proud full sail of his great verse,\n" +
            "Bound for the prize of all too precious you,\n" +
            "That did my ripe thoughts in my brain inhearse,\n" +
            "Making their tomb the womb wherein they grew?\n" +
            "Was it his spirit, by spirits taught to write,\n" +
            "Above a mortal pitch, that struck me dead?\n" +
            "No, neither he, nor his compeers by night\n" +
            "Giving him aid, my verse astonished.\n" +
            "He, nor that affable familiar ghost\n" +
            "Which nightly gulls him with intelligence,\n" +
            "As victors of my silence cannot boast;\n" +
            "I was not sick of any fear from thence:\n" +
            "  But when your countenance fill'd up his line,\n" +
            "  Then lacked I matter; that enfeebled mine. ",

        "Farewell! thou art too dear for my possessing,\n" +
            "And like enough thou know'st thy estimate,\n" +
            "The charter of thy worth gives thee releasing;\n" +
            "My bonds in thee are all determinate.\n" +
            "For how do I hold thee but by thy granting?\n" +
            "And for that riches where is my deserving?\n" +
            "The cause of this fair gift in me is wanting,\n" +
            "And so my patent back again is swerving.\n" +
            "Thy self thou gav'st, thy own worth then not knowing,\n" +
            "Or me to whom thou gav'st it, else mistaking;\n" +
            "So thy great gift, upon misprision growing,\n" +
            "Comes home again, on better judgement making.\n" +
            "  Thus have I had thee, as a dream doth flatter,\n" +
            "  In sleep a king, but waking no such matter. ",

        "When thou shalt be dispos'd to set me light,\n" +
            "And place my merit in the eye of scorn,\n" +
            "Upon thy side, against myself I'll fight,\n" +
            "And prove thee virtuous, though thou art forsworn.\n" +
            "With mine own weakness, being best acquainted,\n" +
            "Upon thy part I can set down a story\n" +
            "Of faults conceal'd, wherein I am attainted;\n" +
            "That thou in losing me shalt win much glory:\n" +
            "And I by this will be a gainer too;\n" +
            "For bending all my loving thoughts on thee,\n" +
            "The injuries that to myself I do,\n" +
            "Doing thee vantage, double-vantage me.\n" +
            "  Such is my love, to thee I so belong,\n" +
            "  That for thy right, myself will bear all wrong. ",

        "Say that thou didst forsake me for some fault,\n" +
            "And I will comment upon that offence:\n" +
            "Speak of my lameness, and I straight will halt,\n" +
            "Against thy reasons making no defence.\n" +
            "Thou canst not love disgrace me half so ill,\n" +
            "To set a form upon desired change,\n" +
            "As I'll myself disgrace; knowing thy will,\n" +
            "I will acquaintance strangle, and look strange;\n" +
            "Be absent from thy walks; and in my tongue\n" +
            "Thy sweet beloved name no more shall dwell,\n" +
            "Lest I, too much profane, should do it wrong,\n" +
            "And haply of our old acquaintance tell.\n" +
            "  For thee, against my self I'll vow debate,\n" +
            "  For I must ne'er love him whom thou dost hate. ",

        "Then hate me when thou wilt; if ever, now;\n" +
            "Now, while the world is bent my deeds to cross,\n" +
            "Join with the spite of fortune, make me bow,\n" +
            "And do not drop in for an after-loss:\n" +
            "Ah! do not, when my heart hath 'scap'd this sorrow,\n" +
            "Come in the rearward of a conquer'd woe;\n" +
            "Give not a windy night a rainy morrow,\n" +
            "To linger out a purpos'd overthrow.\n" +
            "If thou wilt leave me, do not leave me last,\n" +
            "When other petty griefs have done their spite,\n" +
            "But in the onset come: so shall I taste\n" +
            "At first the very worst of fortune's might;\n" +
            "  And other strains of woe, which now seem woe,\n" +
            "  Compar'd with loss of thee, will not seem so. ",

        "Some glory in their birth, some in their skill,\n" +
            "Some in their wealth, some in their body's force,\n" +
            "Some in their garments though new-fangled ill;\n" +
            "Some in their hawks and hounds, some in their horse;\n" +
            "And every humour hath his adjunct pleasure,\n" +
            "Wherein it finds a joy above the rest:\n" +
            "But these particulars are not my measure,\n" +
            "All these I better in one general best.\n" +
            "Thy love is better than high birth to me,\n" +
            "Richer than wealth, prouder than garments' costs,\n" +
            "Of more delight than hawks and horses be;\n" +
            "And having thee, of all men's pride I boast:\n" +
            "  Wretched in this alone, that thou mayst take\n" +
            "  All this away, and me most wretchcd make. ",

        "But do thy worst to steal thyself away,\n" +
            "For term of life thou art assured mine;\n" +
            "And life no longer than thy love will stay,\n" +
            "For it depends upon that love of thine.\n" +
            "Then need I not to fear the worst of wrongs,\n" +
            "When in the least of them my life hath end.\n" +
            "I see a better state to me belongs\n" +
            "Than that which on thy humour doth depend:\n" +
            "Thou canst not vex me with inconstant mind,\n" +
            "Since that my life on thy revolt doth lie.\n" +
            "O! what a happy title do I find,\n" +
            "Happy to have thy love, happy to die!\n" +
            "  But what's so blessed-fair that fears no blot?\n" +
            "  Thou mayst be false, and yet I know it not. ",

        "So shall I live, supposing thou art true,\n" +
            "Like a deceived husband; so love's face\n" +
            "May still seem love to me, though alter'd new;\n" +
            "Thy looks with me, thy heart in other place:\n" +
            "For there can live no hatred in thine eye,\n" +
            "Therefore in that I cannot know thy change.\n" +
            "In many's looks, the false heart's history\n" +
            "Is writ in moods, and frowns, and wrinkles strange.\n" +
            "But heaven in thy creation did decree\n" +
            "That in thy face sweet love should ever dwell;\n" +
            "Whate'er thy thoughts, or thy heart's workings be,\n" +
            "Thy looks should nothing thence, but sweetness tell.\n" +
            "  How like Eve's apple doth thy beauty grow,\n" +
            "  If thy sweet virtue answer not thy show! ",

        "They that have power to hurt, and will do none,\n" +
            "That do not do the thing they most do show,\n" +
            "Who, moving others, are themselves as stone,\n" +
            "Unmoved, cold, and to temptation slow;\n" +
            "They rightly do inherit heaven's graces,\n" +
            "And husband nature's riches from expense;\n" +
            "They are the lords and owners of their faces,\n" +
            "Others, but stewards of their excellence.\n" +
            "The summer's flower is to the summer sweet,\n" +
            "Though to itself, it only live and die,\n" +
            "But if that flower with base infection meet,\n" +
            "The basest weed outbraves his dignity:\n" +
            "  For sweetest things turn sourest by their deeds;\n" +
            "  Lilies that fester, smell far worse than weeds. ",

        "How sweet and lovely dost thou make the shame\n" +
            "Which, like a canker in the fragrant rose,\n" +
            "Doth spot the beauty of thy budding name!\n" +
            "O! in what sweets dost thou thy sins enclose.\n" +
            "That tongue that tells the story of thy days,\n" +
            "Making lascivious comments on thy sport,\n" +
            "Cannot dispraise, but in a kind of praise;\n" +
            "Naming thy name, blesses an ill report.\n" +
            "O! what a mansion have those vices got\n" +
            "Which for their habitation chose out thee,\n" +
            "Where beauty's veil doth cover every blot\n" +
            "And all things turns to fair that eyes can see!\n" +
            "  Take heed, dear heart, of this large privilege;\n" +
            "  The hardest knife ill-us'd doth lose his edge. ",

        "Some say thy fault is youth, some wantonness;\n" +
            "Some say thy grace is youth and gentle sport;\n" +
            "Both grace and faults are lov'd of more and less:\n" +
            "Thou mak'st faults graces that to thee resort.\n" +
            "As on the finger of a throned queen\n" +
            "The basest jewel will be well esteem'd,\n" +
            "So are those errors that in thee are seen\n" +
            "To truths translated, and for true things deem'd.\n" +
            "How many lambs might the stern wolf betray,\n" +
            "If like a lamb he could his looks translate!\n" +
            "How many gazers mightst thou lead away,\n" +
            "if thou wouldst use the strength of all thy state!\n" +
            "  But do not so; I love thee in such sort,\n" +
            "  As, thou being mine, mine is thy good report. ",

        "How like a winter hath my absence been\n" +
            "From thee, the pleasure of the fleeting year!\n" +
            "What freezings have I felt, what dark days seen!\n" +
            "What old December's bareness everywhere!\n" +
            "And yet this time removed was summer's time;\n" +
            "The teeming autumn, big with rich increase,\n" +
            "Bearing the wanton burden of the prime,\n" +
            "Like widow'd wombs after their lords' decease:\n" +
            "Yet this abundant issue seem'd to me\n" +
            "But hope of orphans, and unfather'd fruit;\n" +
            "For summer and his pleasures wait on thee,\n" +
            "And, thou away, the very birds are mute:\n" +
            "  Or, if they sing, 'tis with so dull a cheer,\n" +
            "  That leaves look pale, dreading the winter's near. ",

        "From you have I been absent in the spring,\n" +
            "When proud-pied April, dress'd in all his trim,\n" +
            "Hath put a spirit of youth in every thing,\n" +
            "That heavy Saturn laugh'd and leap'd with him.\n" +
            "Yet nor the lays of birds, nor the sweet smell\n" +
            "Of different flowers in odour and in hue,\n" +
            "Could make me any summer's story tell,\n" +
            "Or from their proud lap pluck them where they grew:\n" +
            "Nor did I wonder at the lily's white,\n" +
            "Nor praise the deep vermilion in the rose;\n" +
            "They were but sweet, but figures of delight,\n" +
            "Drawn after you, you pattern of all those.\n" +
            "  Yet seem'd it winter still, and you away,\n" +
            "  As with your shadow I with these did play. ",

        "The forward violet thus did I chide:\n" +
            "Sweet thief, whence didst thou steal thy sweet that smells,\n" +
            "If not from my love's breath? The purple pride\n" +
            "Which on thy soft cheek for complexion dwells\n" +
            "In my love's veins thou hast too grossly dy'd.\n" +
            "The lily I condemned for thy hand,\n" +
            "And buds of marjoram had stol'n thy hair;\n" +
            "The roses fearfully on thorns did stand,\n" +
            "One blushing shame, another white despair;\n" +
            "A third, nor red nor white, had stol'n of both,\n" +
            "And to his robbery had annex'd thy breath;\n" +
            "But, for his theft, in pride of all his growth\n" +
            "A vengeful canker eat him up to death.\n" +
            "  More flowers I noted, yet I none could see,\n" +
            "  But sweet, or colour it had stol'n from thee. ",

        "Where art thou Muse that thou forget'st so long,\n" +
            "To speak of that which gives thee all thy might?\n" +
            "Spend'st thou thy fury on some worthless song,\n" +
            "Darkening thy power to lend base subjects light?\n" +
            "Return forgetful Muse, and straight redeem,\n" +
            "In gentle numbers time so idly spent;\n" +
            "Sing to the ear that doth thy lays esteem\n" +
            "And gives thy pen both skill and argument.\n" +
            "Rise, resty Muse, my love's sweet face survey,\n" +
            "If Time have any wrinkle graven there;\n" +
            "If any, be a satire to decay,\n" +
            "And make time's spoils despised every where.\n" +
            "  Give my love fame faster than Time wastes life,\n" +
            "  So thou prevent'st his scythe and crooked knife. ",

        "O truant Muse what shall be thy amends\n" +
            "For thy neglect of truth in beauty dy'd?\n" +
            "Both truth and beauty on my love depends;\n" +
            "So dost thou too, and therein dignified.\n" +
            "Make answer Muse: wilt thou not haply say,\n" +
            "'Truth needs no colour, with his colour fix'd;\n" +
            "Beauty no pencil, beauty's truth to lay;\n" +
            "But best is best, if never intermix'd'?\n" +
            "Because he needs no praise, wilt thou be dumb?\n" +
            "Excuse not silence so, for't lies in thee\n" +
            "To make him much outlive a gilded tomb\n" +
            "And to be prais'd of ages yet to be.\n" +
            "  Then do thy office, Muse; I teach thee how\n" +
            "  To make him seem long hence as he shows now. ",

        "My love is strengthen'd, though more weak in seeming;\n" +
            "I love not less, though less the show appear;\n" +
            "That love is merchandiz'd, whose rich esteeming,\n" +
            "The owner's tongue doth publish every where.\n" +
            "Our love was new, and then but in the spring,\n" +
            "When I was wont to greet it with my lays;\n" +
            "As Philomel in summer's front doth sing,\n" +
            "And stops her pipe in growth of riper days:\n" +
            "Not that the summer is less pleasant now\n" +
            "Than when her mournful hymns did hush the night,\n" +
            "But that wild music burthens every bough,\n" +
            "And sweets grown common lose their dear delight.\n" +
            "  Therefore like her, I sometime hold my tongue:\n" +
            "  Because I would not dull you with my song. ",

        "Alack! what poverty my Muse brings forth,\n" +
            "That having such a scope to show her pride,\n" +
            "The argument, all bare, is of more worth\n" +
            "Than when it hath my added praise beside!\n" +
            "O! blame me not, if I no more can write!\n" +
            "Look in your glass, and there appears a face\n" +
            "That over-goes my blunt invention quite,\n" +
            "Dulling my lines, and doing me disgrace.\n" +
            "Were it not sinful then, striving to mend,\n" +
            "To mar the subject that before was well?\n" +
            "For to no other pass my verses tend\n" +
            "Than of your graces and your gifts to tell;\n" +
            "  And more, much more, than in my verse can sit,\n" +
            "  Your own glass shows you when you look in it. ",

        "To me, fair friend, you never can be old,\n" +
            "For as you were when first your eye I ey'd,\n" +
            "Such seems your beauty still. Three winters cold,\n" +
            "Have from the forests shook three summers' pride,\n" +
            "Three beauteous springs to yellow autumn turn'd,\n" +
            "In process of the seasons have I seen,\n" +
            "Three April perfumes in three hot Junes burn'd,\n" +
            "Since first I saw you fresh, which yet are green.\n" +
            "Ah! yet doth beauty like a dial-hand,\n" +
            "Steal from his figure, and no pace perceiv'd;\n" +
            "So your sweet hue, which methinks still doth stand,\n" +
            "Hath motion, and mine eye may be deceiv'd:\n" +
            "  For fear of which, hear this thou age unbred:\n" +
            "  Ere you were born was beauty's summer dead. ",

        "Let not my love be call'd idolatry,\n" +
            "Nor my beloved as an idol show,\n" +
            "Since all alike my songs and praises be\n" +
            "To one, of one, still such, and ever so.\n" +
            "Kind is my love to-day, to-morrow kind,\n" +
            "Still constant in a wondrous excellence;\n" +
            "Therefore my verse to constancy confin'd,\n" +
            "One thing expressing, leaves out difference.\n" +
            "'Fair, kind, and true,' is all my argument,\n" +
            "'Fair, kind, and true,' varying to other words;\n" +
            "And in this change is my invention spent,\n" +
            "Three themes in one, which wondrous scope affords.\n" +
            "  Fair, kind, and true, have often liv'd alone,\n" +
            "  Which three till now, never kept seat in one. ",

        "When in the chronicle of wasted time\n" +
            "I see descriptions of the fairest wights,\n" +
            "And beauty making beautiful old rime,\n" +
            "In praise of ladies dead and lovely knights,\n" +
            "Then, in the blazon of sweet beauty's best,\n" +
            "Of hand, of foot, of lip, of eye, of brow,\n" +
            "I see their antique pen would have express'd\n" +
            "Even such a beauty as you master now.\n" +
            "So all their praises are but prophecies\n" +
            "Of this our time, all you prefiguring;\n" +
            "And for they looked but with divining eyes,\n" +
            "They had not skill enough your worth to sing:\n" +
            "  For we, which now behold these present days,\n" +
            "  Have eyes to wonder, but lack tongues to praise. ",

        "Not mine own fears, nor the prophetic soul\n" +
            "Of the wide world dreaming on things to come,\n" +
            "Can yet the lease of my true love control,\n" +
            "Supposed as forfeit to a confin'd doom.\n" +
            "The mortal moon hath her eclipse endur'd,\n" +
            "And the sad augurs mock their own presage;\n" +
            "Incertainties now crown themselves assur'd,\n" +
            "And peace proclaims olives of endless age.\n" +
            "Now with the drops of this most balmy time,\n" +
            "My love looks fresh, and Death to me subscribes,\n" +
            "Since, spite of him, I'll live in this poor rime,\n" +
            "While he insults o'er dull and speechless tribes:\n" +
            "  And thou in this shalt find thy monument,\n" +
            "  When tyrants' crests and tombs of brass are spent. ",

        "What's in the brain, that ink may character,\n" +
            "Which hath not figur'd to thee my true spirit?\n" +
            "What's new to speak, what now to register,\n" +
            "That may express my love, or thy dear merit?\n" +
            "Nothing, sweet boy; but yet, like prayers divine,\n" +
            "I must each day say o'er the very same;\n" +
            "Counting no old thing old, thou mine, I thine,\n" +
            "Even as when first I hallow'd thy fair name.\n" +
            "So that eternal love in love's fresh case,\n" +
            "Weighs not the dust and injury of age,\n" +
            "Nor gives to necessary wrinkles place,\n" +
            "But makes antiquity for aye his page;\n" +
            "  Finding the first conceit of love there bred,\n" +
            "  Where time and outward form would show it dead. ",

        "O! never say that I was false of heart,\n" +
            "Though absence seem'd my flame to qualify,\n" +
            "As easy might I from my self depart\n" +
            "As from my soul which in thy breast doth lie:\n" +
            "That is my home of love: if I have rang'd,\n" +
            "Like him that travels, I return again;\n" +
            "Just to the time, not with the time exchang'd,\n" +
            "So that myself bring water for my stain.\n" +
            "Never believe though in my nature reign'd,\n" +
            "All frailties that besiege all kinds of blood,\n" +
            "That it could so preposterously be stain'd,\n" +
            "To leave for nothing all thy sum of good;\n" +
            "  For nothing this wide universe I call,\n" +
            "  Save thou, my rose, in it thou art my all. ",

        "Alas! 'tis true, I have gone here and there,\n" +
            "And made my self a motley to the view,\n" +
            "Gor'd mine own thoughts, sold cheap what is most dear,\n" +
            "Made old offences of affections new;\n" +
            "Most true it is, that I have look'd on truth\n" +
            "Askance and strangely; but, by all above,\n" +
            "These blenches gave my heart another youth,\n" +
            "And worse essays prov'd thee my best of love.\n" +
            "Now all is done, save what shall have no end:\n" +
            "Mine appetite I never more will grind\n" +
            "On newer proof, to try an older friend,\n" +
            "A god in love, to whom I am confin'd.\n" +
            "  Then give me welcome, next my heaven the best,\n" +
            "  Even to thy pure and most most loving breast. ",

        "O! for my sake do you with Fortune chide,\n" +
            "The guilty goddess of my harmful deeds,\n" +
            "That did not better for my life provide\n" +
            "Than public means which public manners breeds.\n" +
            "Thence comes it that my name receives a brand,\n" +
            "And almost thence my nature is subdu'd\n" +
            "To what it works in, like the dyer's hand:\n" +
            "Pity me, then, and wish I were renew'd;\n" +
            "Whilst, like a willing patient, I will drink,\n" +
            "Potions of eisel 'gainst my strong infection;\n" +
            "No bitterness that I will bitter think,\n" +
            "Nor double penance, to correct correction.\n" +
            "  Pity me then, dear friend, and I assure ye,\n" +
            "  Even that your pity is enough to cure me. ",

        "Your love and pity doth the impression fill,\n" +
            "Which vulgar scandal stamp'd upon my brow;\n" +
            "For what care I who calls me well or ill,\n" +
            "So you o'er-green my bad, my good allow?\n" +
            "You are my all-the-world, and I must strive\n" +
            "To know my shames and praises from your tongue;\n" +
            "None else to me, nor I to none alive,\n" +
            "That my steel'd sense or changes right or wrong.\n" +
            "In so profound abysm I throw all care\n" +
            "Of others' voices, that my adder's sense\n" +
            "To critic and to flatterer stopped are.\n" +
            "Mark how with my neglect I do dispense:\n" +
            "  You are so strongly in my purpose bred,\n" +
            "  That all the world besides methinks are dead. ",

        "Since I left you, mine eye is in my mind;\n" +
            "And that which governs me to go about\n" +
            "Doth part his function and is partly blind,\n" +
            "Seems seeing, but effectually is out;\n" +
            "For it no form delivers to the heart\n" +
            "Of bird, of flower, or shape which it doth latch:\n" +
            "Of his quick objects hath the mind no part,\n" +
            "Nor his own vision holds what it doth catch;\n" +
            "For if it see the rud'st or gentlest sight,\n" +
            "The most sweet favour or deformed'st creature,\n" +
            "The mountain or the sea, the day or night:\n" +
            "The crow, or dove, it shapes them to your feature.\n" +
            "  Incapable of more, replete with you,\n" +
            "  My most true mind thus maketh mine untrue. ",

        "Or whether doth my mind, being crown'd with you,\n" +
            "Drink up the monarch's plague, this flattery?\n" +
            "Or whether shall I say, mine eye saith true,\n" +
            "And that your love taught it this alchemy,\n" +
            "To make of monsters and things indigest\n" +
            "Such cherubins as your sweet self resemble,\n" +
            "Creating every bad a perfect best,\n" +
            "As fast as objects to his beams assemble?\n" +
            "O! 'tis the first, 'tis flattery in my seeing,\n" +
            "And my great mind most kingly drinks it up:\n" +
            "Mine eye well knows what with his gust is 'greeing,\n" +
            "And to his palate doth prepare the cup:\n" +
            "  If it be poison'd, 'tis the lesser sin\n" +
            "  That mine eye loves it and doth first begin. ",

        "Those lines that I before have writ do lie,\n" +
            "Even those that said I could not love you dearer:\n" +
            "Yet then my judgment knew no reason why\n" +
            "My most full flame should afterwards burn clearer.\n" +
            "But reckoning Time, whose million'd accidents\n" +
            "Creep in 'twixt vows, and change decrees of kings,\n" +
            "Tan sacred beauty, blunt the sharp'st intents,\n" +
            "Divert strong minds to the course of altering things;\n" +
            "Alas! why fearing of Time's tyranny,\n" +
            "Might I not then say, 'Now I love you best,'\n" +
            "When I was certain o'er incertainty,\n" +
            "Crowning the present, doubting of the rest?\n" +
            "  Love is a babe, then might I not say so,\n" +
            "  To give full growth to that which still doth grow? ",

        "Let me not to the marriage of true minds\n" +
            "Admit impediments. Love is not love\n" +
            "Which alters when it alteration finds,\n" +
            "Or bends with the remover to remove:\n" +
            "O, no! it is an ever-fixed mark,\n" +
            "That looks on tempests and is never shaken;\n" +
            "It is the star to every wandering bark,\n" +
            "Whose worth's unknown, although his height be taken.\n" +
            "Love's not Time's fool, though rosy lips and cheeks\n" +
            "Within his bending sickle's compass come;\n" +
            "Love alters not with his brief hours and weeks,\n" +
            "But bears it out even to the edge of doom.\n" +
            "  If this be error and upon me prov'd,\n" +
            "  I never writ, nor no man ever lov'd. ",

        "Accuse me thus: that I have scanted all,\n" +
            "Wherein I should your great deserts repay,\n" +
            "Forgot upon your dearest love to call,\n" +
            "Whereto all bonds do tie me day by day;\n" +
            "That I have frequent been with unknown minds,\n" +
            "And given to time your own dear-purchas'd right;\n" +
            "That I have hoisted sail to all the winds\n" +
            "Which should transport me farthest from your sight.\n" +
            "Book both my wilfulness and errors down,\n" +
            "And on just proof surmise, accumulate;\n" +
            "Bring me within the level of your frown,\n" +
            "But shoot not at me in your waken'd hate;\n" +
            "  Since my appeal says I did strive to prove\n" +
            "  The constancy and virtue of your love. ",

        "Like as, to make our appetite more keen,\n" +
            "With eager compounds we our palate urge;\n" +
            "As, to prevent our maladies unseen,\n" +
            "We sicken to shun sickness when we purge;\n" +
            "Even so, being full of your ne'er-cloying sweetness,\n" +
            "To bitter sauces did I frame my feeding;\n" +
            "And, sick of welfare, found a kind of meetness\n" +
            "To be diseas'd, ere that there was true needing.\n" +
            "Thus policy in love, to anticipate\n" +
            "The ills that were not, grew to faults assur'd,\n" +
            "And brought to medicine a healthful state\n" +
            "Which, rank of goodness, would by ill be cur'd;\n" +
            "  But thence I learn and find the lesson true,\n" +
            "  Drugs poison him that so fell sick of you. ",

        "What potions have I drunk of Siren tears,\n" +
            "Distill'd from limbecks foul as hell within,\n" +
            "Applying fears to hopes, and hopes to fears,\n" +
            "Still losing when I saw myself to win!\n" +
            "What wretched errors hath my heart committed,\n" +
            "Whilst it hath thought itself so blessed never!\n" +
            "How have mine eyes out of their spheres been fitted,\n" +
            "In the distraction of this madding fever!\n" +
            "O benefit of ill! now I find true\n" +
            "That better is, by evil still made better;\n" +
            "And ruin'd love, when it is built anew,\n" +
            "Grows fairer than at first, more strong, far greater.\n" +
            "  So I return rebuk'd to my content,\n" +
            "  And gain by ill thrice more than I have spent. ",

        "That you were once unkind befriends me now,\n" +
            "And for that sorrow, which I then did feel,\n" +
            "Needs must I under my transgression bow,\n" +
            "Unless my nerves were brass or hammer'd steel.\n" +
            "For if you were by my unkindness shaken,\n" +
            "As I by yours, you've pass'd a hell of time;\n" +
            "And I, a tyrant, have no leisure taken\n" +
            "To weigh how once I suffer'd in your crime.\n" +
            "O! that our night of woe might have remember'd\n" +
            "My deepest sense, how hard true sorrow hits,\n" +
            "And soon to you, as you to me, then tender'd\n" +
            "The humble salve, which wounded bosoms fits!\n" +
            "  But that your trespass now becomes a fee;\n" +
            "  Mine ransoms yours, and yours must ransom me. ",

        "'Tis better to be vile than vile esteem'd,\n" +
            "When not to be receives reproach of being;\n" +
            "And the just pleasure lost, which is so deem'd\n" +
            "Not by our feeling, but by others' seeing:\n" +
            "For why should others' false adulterate eyes\n" +
            "Give salutation to my sportive blood?\n" +
            "Or on my frailties why are frailer spies,\n" +
            "Which in their wills count bad what I think good?\n" +
            "No, I am that I am, and they that level\n" +
            "At my abuses reckon up their own:\n" +
            "I may be straight though they themselves be bevel;\n" +
            "By their rank thoughts, my deeds must not be shown;\n" +
            "  Unless this general evil they maintain,\n" +
            "  All men are bad and in their doBadness reign. ",

        "Thy gift, thy tables, are within my brain\n" +
            "Full character'd with lasting memory,\n" +
            "Which shall above that idle rank remain,\n" +
            "Beyond all date; even to eternity:\n" +
            "Or, at the least, so long as brain and heart\n" +
            "Have faculty by nature to subsist;\n" +
            "Till each to raz'd oblivion yield his part\n" +
            "Of thee, thy record never can be miss'd.\n" +
            "That poor retention could not so much hold,\n" +
            "Nor need I tallies thy dear love to score;\n" +
            "Therefore to give them from me was I bold,\n" +
            "To trust those tables that receive thee more:\n" +
            "  To keep an adjunct to remember thee\n" +
            "  Were to import forgetfulness in me. ",

        "No, Time, thou shalt not boast that I do change:\n" +
            "Thy pyramids built up with newer might\n" +
            "To me are nothing novel, nothing strange;\n" +
            "They are but dressings of a former sight.\n" +
            "Our dates are brief, and therefore we admire\n" +
            "What thou dost foist upon us that is old;\n" +
            "And rather make them born to our desire\n" +
            "Than think that we before have heard them told.\n" +
            "Thy registers and thee I both defy,\n" +
            "Not wondering at the present nor the past,\n" +
            "For thy records and what we see doth lie,\n" +
            "Made more or less by thy continual haste.\n" +
            "  This I do vow and this shall ever be;\n" +
            "  I will be true despite thy scythe and thee. ",

        "If my dear love were but the child of state,\n" +
            "It might for Fortune's bastard be unfather'd,\n" +
            "As subject to Time's love or to Time's hate,\n" +
            "Weeds among weeds, or flowers with flowers gather'd.\n" +
            "No, it was builded far from accident;\n" +
            "It suffers not in smiling pomp, nor falls\n" +
            "Under the blow of thralled discontent,\n" +
            "Whereto th' inviting time our fashion calls:\n" +
            "It fears not policy, that heretic,\n" +
            "Which works on leases of short-number'd hours,\n" +
            "But all alone stands hugely politic,\n" +
            "That it nor grows with heat, nor drowns with showers.\n" +
            "  To this I witness call the fools of time,\n" +
            "  Which die for goodness, who have lived for crime. ",

        "Were't aught to me I bore the canopy,\n" +
            "With my extern the outward honouring,\n" +
            "Or laid great bases for eternity,\n" +
            "Which proves more short than waste or ruining?\n" +
            "Have I not seen dwellers on form and favour\n" +
            "Lose all and more by paying too much rent\n" +
            "For compound sweet; forgoing simple savour,\n" +
            "Pitiful thrivers, in their gazing spent?\n" +
            "No; let me be obsequious in thy heart,\n" +
            "And take thou my oblation, poor but free,\n" +
            "Which is not mix'd with seconds, knows no art,\n" +
            "But mutual render, only me for thee.\n" +
            "  Hence, thou suborned informer! a true soul\n" +
            "  When most impeach'd, stands least in thy control. ",

        "O thou, my lovely boy, who in thy power\n" +
            "Dost hold Time's fickle glass, his fickle hour;\n" +
            "Who hast by waning grown, and therein show'st\n" +
            "Thy lovers withering, as thy sweet self grow'st.\n" +
            "If Nature, sovereign mistress over wrack,\n" +
            "As thou goest onwards, still will pluck thee back,\n" +
            "She keeps thee to this purpose, that her skill\n" +
            "May time disgrace and wretched minutes kill.\n" +
            "Yet fear her, O thou minion of her pleasure!\n" +
            "She may detain, but not still keep, her treasure:\n" +
            "  Her audit (though delayed) answered must be,\n" +
            "  And her quietus is to render thee. ",

        "In the old age black was not counted fair,\n" +
            "Or if it were, it bore not beauty's name;\n" +
            "But now is black beauty's successive heir,\n" +
            "And beauty slander'd with a bastard shame:\n" +
            "For since each hand hath put on Nature's power,\n" +
            "Fairing the foul with Art's false borrowed face,\n" +
            "Sweet beauty hath no name, no holy bower,\n" +
            "But is profan'd, if not lives in disgrace.\n" +
            "Therefore my mistress' eyes are raven black,\n" +
            "Her eyes so suited, and they mourners seem\n" +
            "At such who, not born fair, no beauty lack,\n" +
            "Sland'ring creation with a false esteem:\n" +
            "  Yet so they mourn becoming of their woe,\n" +
            "  That every tongue says beauty should look so. ",

        "How oft when thou, my music, music play'st,\n" +
            "Upon that blessed wood whose motion sounds\n" +
            "With thy sweet fingers when thou gently sway'st\n" +
            "The wiry concord that mine ear confounds,\n" +
            "Do I envy those jacks that nimble leap,\n" +
            "To kiss the tender inward of thy hand,\n" +
            "Whilst my poor lips which should that harvest reap,\n" +
            "At the wood's boldness by thee blushing stand!\n" +
            "To be so tickled, they would change their state\n" +
            "And situation with those dancing chips,\n" +
            "O'er whom thy fingers walk with gentle gait,\n" +
            "Making dead wood more bless'd than living lips.\n" +
            "  Since saucy jacks so happy are in this,\n" +
            "  Give them thy fingers, me thy lips to kiss. ",

        "The expense of spirit in a waste of shame\n" +
            "Is lust in action: and till action, lust\n" +
            "Is perjur'd, murderous, bloody, full of blame,\n" +
            "Savage, extreme, rude, cruel, not to trust;\n" +
            "Enjoy'd no sooner but despised straight;\n" +
            "Past reason hunted; and no sooner had,\n" +
            "Past reason hated, as a swallow'd bait,\n" +
            "On purpose laid to make the taker mad:\n" +
            "Mad in pursuit and in possession so;\n" +
            "Had, having, and in quest, to have extreme;\n" +
            "A bliss in proof,-- and prov'd, a very woe;\n" +
            "Before, a joy propos'd; behind a dream.\n" +
            "  All this the world well knows; yet none knows well\n" +
            "  To shun the heaven that leads men to this hell. ",

        "My mistress' eyes are nothing like the sun;\n" +
            "Coral is far more red, than her lips red:\n" +
            "If snow be white, why then her breasts are dun;\n" +
            "If hairs be wires, black wires grow on her head.\n" +
            "I have seen roses damask'd, red and white,\n" +
            "But no such roses see I in her cheeks;\n" +
            "And in some perfumes is there more delight\n" +
            "Than in the breath that from my mistress reeks.\n" +
            "I love to hear her speak, yet well I know\n" +
            "That music hath a far more pleasing sound:\n" +
            "I grant I never saw a goddess go,--\n" +
            "My mistress, when she walks, treads on the ground:\n" +
            "  And yet by heaven, I think my love as rare,\n" +
            "  As any she belied with false compare. ",

        "Thou art as tyrannous, so as thou art,\n" +
            "As those whose beauties proudly make them cruel;\n" +
            "For well thou know'st to my dear doting heart\n" +
            "Thou art the fairest and most precious jewel.\n" +
            "Yet, in good faith, some say that thee behold,\n" +
            "Thy face hath not the power to make love groan;\n" +
            "To say they err I dare not be so bold,\n" +
            "Although I swear it to myself alone.\n" +
            "And to be sure that is not false I swear,\n" +
            "A thousand groans, but thinking on thy face,\n" +
            "One on another's neck, do witness bear\n" +
            "Thy black is fairest in my judgment's place.\n" +
            "  In nothing art thou black save in thy deeds,\n" +
            "  And thence this slander, as I think, proceeds. ",

        "Thine eyes I love, and they, as pitying me,\n" +
            "Knowing thy heart torment me with disdain,\n" +
            "Have put on black and loving mourners be,\n" +
            "Looking with pretty ruth upon my pain.\n" +
            "And truly not the morning sun of heaven\n" +
            "Better becomes the grey cheeks of the east,\n" +
            "Nor that full star that ushers in the even,\n" +
            "Doth half that glory to the sober west,\n" +
            "As those two mourning eyes become thy face:\n" +
            "O! let it then as well beseem thy heart\n" +
            "To mourn for me since mourning doth thee grace,\n" +
            "And suit thy pity like in every part.\n" +
            "  Then will I swear beauty herself is black,\n" +
            "  And all they foul that thy complexion lack. ",

        "Beshrew that heart that makes my heart to groan\n" +
            "For that deep wound it gives my friend and me!\n" +
            "Is't not enough to torture me alone,\n" +
            "But slave to slavery my sweet'st friend must be?\n" +
            "Me from myself thy cruel eye hath taken,\n" +
            "And my next self thou harder hast engross'd:\n" +
            "Of him, myself, and thee I am forsaken;\n" +
            "A torment thrice three-fold thus to be cross'd:\n" +
            "Prison my heart in thy steel bosom's ward,\n" +
            "But then my friend's heart let my poor heart bail;\n" +
            "Whoe'er keeps me, let my heart be his guard;\n" +
            "Thou canst not then use rigour in my jail:\n" +
            "  And yet thou wilt; for I, being pent in thee,\n" +
            "  Perforce am thine, and all that is in me. ",

        "So, now I have confess'd that he is thine,\n" +
            "And I my self am mortgag'd to thy will,\n" +
            "Myself I'll forfeit, so that other mine\n" +
            "Thou wilt restore to be my comfort still:\n" +
            "But thou wilt not, nor he will not be free,\n" +
            "For thou art covetous, and he is kind;\n" +
            "He learn'd but surety-like to write for me,\n" +
            "Under that bond that him as fast doth bind.\n" +
            "The statute of thy beauty thou wilt take,\n" +
            "Thou usurer, that putt'st forth all to use,\n" +
            "And sue a friend came debtor for my sake;\n" +
            "So him I lose through my unkind abuse.\n" +
            "  Him have I lost; thou hast both him and me:\n" +
            "  He pays the whole, and yet am I not free. ",

        "Whoever hath her wish, thou hast thy 'Will,'\n" +
            "And 'Will' to boot, and 'Will' in over-plus;\n" +
            "More than enough am I that vex'd thee still,\n" +
            "To thy sweet will making addition thus.\n" +
            "Wilt thou, whose will is large and spacious,\n" +
            "Not once vouchsafe to hide my will in thine?\n" +
            "Shall will in others seem right gracious,\n" +
            "And in my will no fair acceptance shine?\n" +
            "The sea, all water, yet receives rain still,\n" +
            "And in abundance addeth to his store;\n" +
            "So thou, being rich in 'Will,' add to thy 'Will'\n" +
            "One will of mine, to make thy large will more.\n" +
            "  Let no unkind 'No' fair beseechers kill;\n" +
            "  Think all but one, and me in that one 'Will.' ",

        "If thy soul check thee that I come so near,\n" +
            "Swear to thy blind soul that I was thy 'Will',\n" +
            "And will, thy soul knows, is admitted there;\n" +
            "Thus far for love, my love-suit, sweet, fulfil.\n" +
            "'Will', will fulfil the treasure of thy love,\n" +
            "Ay, fill it full with wills, and my will one.\n" +
            "In things of great receipt with ease we prove\n" +
            "Among a number one is reckon'd none:\n" +
            "Then in the number let me pass untold,\n" +
            "Though in thy store's account I one must be;\n" +
            "For nothing hold me, so it please thee hold\n" +
            "That nothing me, a something sweet to thee:\n" +
            "  Make but my name thy love, and love that still,\n" +
            "  And then thou lov'st me for my name is 'Will.' ",

        "Thou blind fool, Love, what dost thou to mine eyes,\n" +
            "That they behold, and see not what they see?\n" +
            "They know what beauty is, see where it lies,\n" +
            "Yet what the best is take the worst to be.\n" +
            "If eyes, corrupt by over-partial looks,\n" +
            "Be anchor'd in the bay where all men ride,\n" +
            "Why of eyes' falsehood hast thou forged hooks,\n" +
            "Whereto the judgment of my heart is tied?\n" +
            "Why should my heart think that a several plot,\n" +
            "Which my heart knows the wide world's common place?\n" +
            "Or mine eyes, seeing this, say this is not,\n" +
            "To put fair truth upon so foul a face?\n" +
            "  In things right true my heart and eyes have err'd,\n" +
            "  And to this false plague are they now transferr'd. ",

        "When my love swears that she is made of truth,\n" +
            "I do believe her though I know she lies,\n" +
            "That she might think me some untutor'd youth,\n" +
            "Unlearned in the world's false subtleties.\n" +
            "Thus vainly thinking that she thinks me young,\n" +
            "Although she knows my days are past the best,\n" +
            "Simply I credit her false-speaking tongue:\n" +
            "On both sides thus is simple truth suppressed:\n" +
            "But wherefore says she not she is unjust?\n" +
            "And wherefore say not I that I am old?\n" +
            "O! love's best habit is in seeming trust,\n" +
            "And age in love, loves not to have years told:\n" +
            "  Therefore I lie with her, and she with me,\n" +
            "  And in our faults by lies we flatter'd be. ",

        "O! call not me to justify the wrong\n" +
            "That thy unkindness lays upon my heart;\n" +
            "Wound me not with thine eye, but with thy tongue:\n" +
            "Use power with power, and slay me not by art,\n" +
            "Tell me thou lov'st elsewhere; but in my sight,\n" +
            "Dear heart, forbear to glance thine eye aside:\n" +
            "What need'st thou wound with cunning, when thy might\n" +
            "Is more than my o'erpress'd defence can bide?\n" +
            "Let me excuse thee: ah! my love well knows\n" +
            "Her pretty looks have been mine enemies;\n" +
            "And therefore from my face she turns my foes,\n" +
            "That they elsewhere might dart their injuries:\n" +
            "  Yet do not so; but since I am near slain,\n" +
            "  Kill me outright with looks, and rid my pain. ",

        "Be wise as thou art cruel; do not press\n" +
            "My tongue-tied patience with too much disdain;\n" +
            "Lest sorrow lend me words, and words express\n" +
            "The manner of my pity-wanting pain.\n" +
            "If I might teach thee wit, better it were,\n" +
            "Though not to love, yet, love to tell me so;--\n" +
            "As testy sick men, when their deaths be near,\n" +
            "No news but health from their physicians know;--\n" +
            "For, if I should despair, I should grow mad,\n" +
            "And in my madness might speak ill of thee;\n" +
            "Now this ill-wresting world is grown so bad,\n" +
            "Mad slanderers by mad ears believed be.\n" +
            "  That I may not be so, nor thou belied,\n" +
            "  Bear thine eyes straight, though thy proud heart go wide. ",

        "In faith I do not love thee with mine eyes,\n" +
            "For they in thee a thousand errors note;\n" +
            "But 'tis my heart that loves what they despise,\n" +
            "Who, in despite of view, is pleased to dote.\n" +
            "Nor are mine ears with thy tongue's tune delighted;\n" +
            "Nor tender feeling, to base touches prone,\n" +
            "Nor taste, nor smell, desire to be invited\n" +
            "To any sensual feast with thee alone:\n" +
            "But my five wits nor my five senses can\n" +
            "Dissuade one foolish heart from serving thee,\n" +
            "Who leaves unsway'd the likeness of a man,\n" +
            "Thy proud heart's slave and vassal wretch to be:\n" +
            "  Only my plague thus far I count my gain,\n" +
            "  That she that makes me sin awards me pain. ",

        "Love is my sin, and thy dear virtue hate,\n" +
            "Hate of my sin, grounded on sinful loving:\n" +
            "O! but with mine compare thou thine own state,\n" +
            "And thou shalt find it merits not reproving;\n" +
            "Or, if it do, not from those lips of thine,\n" +
            "That have profan'd their scarlet ornaments\n" +
            "And seal'd false bonds of love as oft as mine,\n" +
            "Robb'd others' beds' revenues of their rents.\n" +
            "Be it lawful I love thee, as thou lov'st those\n" +
            "Whom thine eyes woo as mine importune thee:\n" +
            "Root pity in thy heart, that, when it grows,\n" +
            "Thy pity may deserve to pitied be.\n" +
            "  If thou dost seek to have what thou dost hide,\n" +
            "  By self-example mayst thou be denied! ",

        "Lo, as a careful housewife runs to catch\n" +
            "One of her feather'd creatures broke away,\n" +
            "Sets down her babe, and makes all swift dispatch\n" +
            "In pursuit of the thing she would have stay;\n" +
            "Whilst her neglected child holds her in chase,\n" +
            "Cries to catch her whose busy care is bent\n" +
            "To follow that which flies before her face,\n" +
            "Not prizing her poor infant's discontent;\n" +
            "So runn'st thou after that which flies from thee,\n" +
            "Whilst I thy babe chase thee afar behind;\n" +
            "But if thou catch thy hope, turn back to me,\n" +
            "And play the mother's part, kiss me, be kind;\n" +
            "  So will I pray that thou mayst have thy 'Will,'\n" +
            "  If thou turn back and my loud crying still. ",

        "Two loves I have of comfort and despair,\n" +
            "Which like two spirits do suggest me still:\n" +
            "The better angel is a man right fair,\n" +
            "The worser spirit a woman colour'd ill.\n" +
            "To win me soon to hell, my female evil,\n" +
            "Tempteth my better angel from my side,\n" +
            "And would corrupt my saint to be a devil,\n" +
            "Wooing his purity with her foul pride.\n" +
            "And whether that my angel be turn'd fiend,\n" +
            "Suspect I may, yet not directly tell;\n" +
            "But being both from me, both to each friend,\n" +
            "I guess one angel in another's hell:\n" +
            "  Yet this shall I ne'er know, but live in doubt,\n" +
            "  Till my bad angel fire my good one out. ",

        "Those lips that Love's own hand did make,\n" +
            "Breathed forth the sound that said 'I hate',\n" +
            "To me that languish'd for her sake:\n" +
            "But when she saw my woeful state,\n" +
            "Straight in her heart did mercy come,\n" +
            "Chiding that tongue that ever sweet\n" +
            "Was us'd in giving gentle doom;\n" +
            "And taught it thus anew to greet;\n" +
            "'I hate' she alter'd with an end,\n" +
            "That followed it as gentle day,\n" +
            "Doth follow night, who like a fiend\n" +
            "From heaven to hell is flown away.\n" +
            "  'I hate', from hate away she threw,\n" +
            "  And sav'd my life, saying 'not you'. ",

        "Poor soul, the centre of my sinful earth,\n" +
            "My sinful earth these rebel powers array,\n" +
            "Why dost thou pine within and suffer dearth,\n" +
            "Painting thy outward walls so costly gay?\n" +
            "Why so large cost, having so short a lease,\n" +
            "Dost thou upon thy fading mansion spend?\n" +
            "Shall worms, inheritors of this excess,\n" +
            "Eat up thy charge? Is this thy body's end?\n" +
            "Then soul, live thou upon thy servant's loss,\n" +
            "And let that pine to aggravate thy store;\n" +
            "Buy terms divine in selling hours of dross;\n" +
            "Within be fed, without be rich no more:\n" +
            "  So shall thou feed on Death, that feeds on men,\n" +
            "  And Death once dead, there's no more dying then. ",

        "My love is as a fever longing still,\n" +
            "For that which longer nurseth the disease;\n" +
            "Feeding on that which doth preserve the ill,\n" +
            "The uncertain sickly appetite to please.\n" +
            "My reason, the physician to my love,\n" +
            "Angry that his prescriptions are not kept,\n" +
            "Hath left me, and I desperate now approve\n" +
            "Desire is death, which physic did except.\n" +
            "Past cure I am, now Reason is past care,\n" +
            "And frantic-mad with evermore unrest;\n" +
            "My thoughts and my discourse as madmen's are,\n" +
            "At random from the truth vainly express'd;\n" +
            "  For I have sworn thee fair, and thought thee bright,\n" +
            "  Who art as black as hell, as dark as night. ",

        "O me! what eyes hath Love put in my head,\n" +
            "Which have no correspondence with true sight;\n" +
            "Or, if they have, where is my judgment fled,\n" +
            "That censures falsely what they see aright?\n" +
            "If that be fair whereon my false eyes dote,\n" +
            "What means the world to say it is not so?\n" +
            "If it be not, then love doth well denote\n" +
            "Love's eye is not so true as all men's: no,\n" +
            "How can it? O! how can Love's eye be true,\n" +
            "That is so vexed with watching and with tears?\n" +
            "No marvel then, though I mistake my view;\n" +
            "The sun itself sees not, till heaven clears.\n" +
            "  O cunning Love! with tears thou keep'st me blind,\n" +
            "  Lest eyes well-seeing thy foul faults should find. ",

        "Canst thou, O cruel! say I love thee not,\n" +
            "When I against myself with thee partake?\n" +
            "Do I not think on thee, when I forgot\n" +
            "Am of my self, all tyrant, for thy sake?\n" +
            "Who hateth thee that I do call my friend,\n" +
            "On whom frown'st thou that I do fawn upon,\n" +
            "Nay, if thou lour'st on me, do I not spend\n" +
            "Revenge upon myself with present moan?\n" +
            "What merit do I in my self respect,\n" +
            "That is so proud thy service to despise,\n" +
            "When all my best doth worship thy defect,\n" +
            "Commanded by the motion of thine eyes?\n" +
            "  But, love, hate on, for now I know thy mind;\n" +
            "  Those that can see thou lov'st, and I am blind. ",

        "O! from what power hast thou this powerful might,\n" +
            "With insufficiency my heart to sway?\n" +
            "To make me give the lie to my true sight,\n" +
            "And swear that brightness doth not grace the day?\n" +
            "Whence hast thou this becoming of things ill,\n" +
            "That in the very refuse of thy deeds\n" +
            "There is such strength and warrantise of skill,\n" +
            "That, in my mind, thy worst all best exceeds?\n" +
            "Who taught thee how to make me love thee more,\n" +
            "The more I hear and see just cause of hate?\n" +
            "O! though I love what others do abhor,\n" +
            "With others thou shouldst not abhor my state:\n" +
            "  If thy unworthiness rais'd love in me,\n" +
            "  More worthy I to be belov'd of thee. ",

        "Love is too young to know what conscience is,\n" +
            "Yet who knows not conscience is born of love?\n" +
            "Then, gentle cheater, urge not my amiss,\n" +
            "Lest guilty of my faults thy sweet self prove:\n" +
            "For, thou betraying me, I do betray\n" +
            "My nobler part to my gross body's treason;\n" +
            "My soul doth tell my body that he may\n" +
            "Triumph in love; flesh stays no farther reason,\n" +
            "But rising at thy name doth point out thee,\n" +
            "As his triumphant prize. Proud of this pride,\n" +
            "He is contented thy poor drudge to be,\n" +
            "To stand in thy affairs, fall by thy side.\n" +
            "  No want of conscience hold it that I call\n" +
            "  Her 'love,' for whose dear love I rise and fall. ",

        "In loving thee thou know'st I am forsworn,\n" +
            "But thou art twice forsworn, to me love swearing;\n" +
            "In act thy bed-vow broke, and new faith torn,\n" +
            "In vowing new hate after new love bearing:\n" +
            "But why of two oaths' breach do I accuse thee,\n" +
            "When I break twenty? I am perjur'd most;\n" +
            "For all my vows are oaths but to misuse thee,\n" +
            "And all my honest faith in thee is lost:\n" +
            "For I have sworn deep oaths of thy deep kindness,\n" +
            "Oaths of thy love, thy truth, thy constancy;\n" +
            "And, to enlighten thee, gave eyes to blindness,\n" +
            "Or made them swear against the thing they see;\n" +
            "  For I have sworn thee fair; more perjur'd I,\n" +
            "  To swear against the truth so foul a lie! ",

        "Cupid laid by his brand and fell asleep:\n" +
            "A maid of Dian's this advantage found,\n" +
            "And his love-kindling fire did quickly steep\n" +
            "In a cold valley-fountain of that ground;\n" +
            "Which borrow'd from this holy fire of Love,\n" +
            "A dateless lively heat, still to endure,\n" +
            "And grew a seeting bath, which yet men prove\n" +
            "Against strange maladies a sovereign cure.\n" +
            "But at my mistress' eye Love's brand new-fired,\n" +
            "The boy for trial needs would touch my breast;\n" +
            "I, sick withal, the help of bath desired,\n" +
            "And thither hied, a sad distemper'd guest,\n" +
            "  But found no cure, the bath for my help lies\n" +
            "  Where Cupid got new fire; my mistress' eyes. ",

        "The little Love-god lying once asleep,\n" +
            "Laid by his side his heart-inflaming brand,\n" +
            "Whilst many nymphs that vow'd chaste life to keep\n" +
            "Came tripping by; but in her maiden hand\n" +
            "The fairest votary took up that fire\n" +
            "Which many legions of true hearts had warm'd;\n" +
            "And so the general of hot desire\n" +
            "Was, sleeping, by a virgin hand disarm'd.\n" +
            "This brand she quenched in a cool well by,\n" +
            "Which from Love's fire took heat perpetual,\n" +
            "Growing a bath and healthful remedy,\n" +
            "For men diseas'd; but I, my mistress' thrall,\n" +
            "  Came there for cure and this by that I prove,\n" +
            "  Love's fire heats water, water cools not love. "
    )
}
