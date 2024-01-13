import { shallowMount } from '@vue/test-utils';
import InhalerLogging from 'C:/Users/USER/OneDrive - Imperial College London/Desktop/asthma_project/Asthma_Helper/interface/src/views/InhalerLogging.vue'; // Adjust the path as needed

const LogHistoryMock = {
  template: '<div></div>'
};

describe('InhalerLogging.vue', () => {
  let wrapper;
  const mockDate = new Date('2023-01-29T10:20:30Z');

  beforeEach(() => {
    jest.useFakeTimers('modern');
    jest.setSystemTime(mockDate);
    wrapper = shallowMount(InhalerLogging, {
      mocks: {
        $router: {
          push: jest.fn()
        }
      },
      global: {
        components: {
          LogHistory: LogHistoryMock // Use the LogHistoryMock here
        }
      }
    });
  });
    
  it('resets puffs today when a new day starts', async () => {
    wrapper.setData({ puffsToday: 5, lastResetDay: mockDate.getDay() - 1 });
    await wrapper.vm.resetPuffsDaily();
    expect(wrapper.vm.puffsToday).toBe(0);
  });

  it('does not reset puffs today if the same day', async () => {
    wrapper.setData({ puffsToday: 5, lastResetDay: mockDate.getDay() });
    await wrapper.vm.resetPuffsDaily();
    expect(wrapper.vm.puffsToday).toBe(5);
  });

  it('resets puffs this week when a new week starts', async () => {
    wrapper.setData({ puffsThisWeek: 10, lastResetWeek: mockDate.getDay() - 7 });
    await wrapper.vm.resetPuffsWeekly();
    expect(wrapper.vm.puffsThisWeek).toBe(0);
  });

  it('does not reset puffs this week if the same week', async () => {
    wrapper.setData({ puffsThisWeek: 10, lastResetWeek: mockDate.getDay() });
    await wrapper.vm.resetPuffsWeekly();
    expect(wrapper.vm.puffsThisWeek).toBe(10);
  });

  afterEach(() => {
    jest.useRealTimers();
  });

});
