/*

 */
package GUI;

import Formations.Hero;


public interface HeroListPanel {
    void filterHeroes(String text, FilterPanel.SourceFilter source, Hero.Rarity rarity, boolean includeSelect, boolean includeBosses);
    String getFilterText();
}
