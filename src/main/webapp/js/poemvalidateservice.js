/**
 * Created by Ivan on 5/28/2016.
 */
angular.module('templateapp').service('ValidateService', function(){
    this.poemType = "";
    this.Restrictions = {};
    // how many lines can user use in their poem
    this.Restrictions.linesCount = 0;
    // how many vowels must be inside one 'стопа' 0 - novalidate 2-yamb,horey 3-anap,amhyb,daktil
    this.Restrictions.syllableType = 0;
    // what pattern can be applied to evaluate poem example (9-8-9-8)
    this.Restrictions.vowelPattern = [];



    this.getPoemType = function() {
        return this.PoemType;
    }
    this.getRules = function(type) {
        switch (type) {
            // Two-syllable rhymes
            case 'Ямб':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 2;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Хорей':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 2;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            // Three-syllable rhymes
            case 'Амфибрахий':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 3;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Анапест':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 3;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Дактиль':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 3;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            // Pirozhok and Poroshok
            case 'Порошок':
            {
                this.Restrictions.linesCount = 4;
                this.Restrictions.syllableType = 2;
                this.Restrictions.vowelPattern = [9, 8, 9, 2];
                return this.Restrictions;
                break;
            }

            case 'Пирожок':
            {
                this.Restrictions.linesCount = 4;
                this.Restrictions.syllableType = 2;
                this.Restrictions.vowelPattern = [9, 8, 9, 8];
                return this.Restrictions;
                break;
            }

            // Beliy and bezrifmy
            case 'Белый стих':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Верлибр':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            // Stih v proze and Monorim
            case 'В прозе':
            {
                this.Restrictions.linesCount = 70;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Монорим':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            // Acrostih and telestih
            case 'Акростих':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            case 'Телестих':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }
            // Another
            case 'Вольный':
            {
                this.Restrictions.linesCount = 100;
                this.Restrictions.syllableType = 0;
                this.Restrictions.vowelPattern = [];
                return this.Restrictions;
                break;
            }

            default:
                this.poemType = 'Unknown';
                break;
        }
    }



});

