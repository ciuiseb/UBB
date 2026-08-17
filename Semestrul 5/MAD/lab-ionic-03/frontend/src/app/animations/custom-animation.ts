import { Animation } from '@ionic/angular';
import { createAnimation } from '@ionic/angular';

export const customSlideInWeb: (
  baseEl: any,
  opts?: any
) => Animation = (baseEl, opts) => {

  const enteringEl = opts.enteringEl;


  const entering = createAnimation()
    .addElement(enteringEl)
    .duration(400)
    .easing('ease-out');

  const wrapper = createAnimation()
    .addElement(enteringEl.querySelector('.modal-wrapper') || enteringEl)
    .fromTo('transform', 'translateY(-100%)', 'translateY(0)')
    .fromTo('opacity', 0.2, 1);

  entering.addAnimation([wrapper]);


  const leaving = createAnimation()
    .addElement(opts.leavingEl)
    .duration(300)
    .easing('ease-in')
    .fromTo('opacity', 1, 0)
    .fromTo('transform', 'translateY(0)', 'translateY(-100%)');

  const animation = createAnimation();
  animation.addAnimation([entering, leaving]);

  return animation;
};
